// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app

import android.app.Application
import android.util.Log
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.request.crossfade
import coil3.video.VideoFrameDecoder
import com.datadog.android.Datadog
import com.datadog.android.DatadogSite
import com.datadog.android.core.configuration.Configuration
import com.datadog.android.log.Logger
import com.datadog.android.log.Logs
import com.datadog.android.log.LogsConfiguration
import com.datadog.android.ndk.NdkCrashReports
import com.datadog.android.privacy.TrackingConsent
import com.meta.levinriegner.mediaview.BuildConfig
import com.meta.levinriegner.mediaview.app.logging.DatadogTree
import com.meta.levinriegner.mediaview.app.logging.LogEventMapper
import com.meta.levinriegner.mediaview.app.logging.MediaViewDebugTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MediaViewApplication : Application(), SingletonImageLoader.Factory {

  override fun onCreate() {
    super.onCreate()
    // Logging
    if (BuildConfig.DEBUG) {
      Timber.plant(MediaViewDebugTree())
    } else {
      initDatadog()
    }
  }

  private fun initDatadog() {
    // SDK Configuration
    val environmentName = "production"
    val appVariantName = ""
    val configuration =
        Configuration.Builder(
            clientToken = BuildConfig.DATADOG_CLIENT_TOKEN,
            env = environmentName,
            variant = appVariantName,
        )
            .useSite(DatadogSite.US1)
            .build()
    Datadog.initialize(this, configuration, TrackingConsent.GRANTED)

    // NDK
    NdkCrashReports.enable()

    // Logs
    val logsConfig = LogsConfiguration.Builder().setEventMapper(LogEventMapper()).build()
    Logs.enable(logsConfig)
    val logger =
        Logger.Builder()
            .setNetworkInfoEnabled(false)
            .setLogcatLogsEnabled(false)
            .setBundleWithTraceEnabled(false)
            .setBundleWithRumEnabled(false)
            .setName("MediaView")
            .setRemoteLogThreshold(Log.INFO)
            .build()
    Timber.plant(DatadogTree(logger))
  }

  override fun newImageLoader(context: PlatformContext): ImageLoader {
    return ImageLoader.Builder(context)
        .components { add(VideoFrameDecoder.Factory()) }
        .crossfade(true)
        .build()
  }
}
