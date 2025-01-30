// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.panel_bar

import androidx.compose.ui.unit.dp
import com.meta.levinriegner.mediaview.R
import com.meta.spatial.compose.composePanel
import com.meta.spatial.core.Entity
import com.meta.spatial.runtime.LayerConfig
import com.meta.spatial.runtime.PanelConfigOptions.Companion.DEFAULT_DPI
import com.meta.spatial.toolkit.AppSystemActivity
import com.meta.spatial.toolkit.Grabbable
import com.meta.spatial.toolkit.PanelRegistration
import com.meta.spatial.toolkit.Transform
import com.meta.spatial.toolkit.createPanelEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Thread.sleep
import java.lang.ref.WeakReference
import kotlin.random.Random

class PanelBar(
  appSystemActivity: AppSystemActivity,
  val parent: Entity,
    // Unique registration id for the panel. Prefer using your own id generator.
  private val registrationId: Int = Random.nextInt(Int.MIN_VALUE, Int.MAX_VALUE),
    // TODO: See if we can get this on runtime
  private val parentHeightMeters: Float,
) {

  private val coroutineScope = CoroutineScope(Dispatchers.Main)

  private val activity = WeakReference(appSystemActivity)

  //  private val parent = WeakReference(parent)
  private var entity: Entity? = null

  fun attach() = coroutineScope.launch {
    // Give some time for parent to position
    withContext(Dispatchers.IO) {
      sleep(500L)
    }
    withContext(Dispatchers.Main) {
      register()
      create()
    }
    Timber.i("DONE")
  }

  // TODO: Call destroy
  fun destroy() {
    activity.clear()
    entity?.destroy()
    entity = null
    coroutineScope.cancel()
  }

  private fun register() {
    activity.get()?.let { activity ->
      activity.registerPanel(
          PanelRegistration(registrationId) { _ ->
            config {
              width = dpToMeters(WIDTH_DP + MARGIN_DP * 2)
              height = dpToMeters(HEIGHT_DP + MARGIN_DP * 2)
              layerConfig = LayerConfig()
              enableTransparent = true
              includeGlass = false
              themeResourceId = R.style.Theme_MediaView
            }
            composePanel {
              setContent {
                PanelBarView(WIDTH_DP.dp, HEIGHT_DP.dp)
              }
            }
          },
      )
    } ?: PanelBarConfig.logger.log(Logger.Level.Error, "Failed to register bar, activity is null")
  }

  private fun create() {
    val barPose = parent.getComponent<Transform>().transform.copy()
    barPose.t = barPose.t.copy(
        y = barPose.t.y - parentHeightMeters / 2
            - dpToMeters(HEIGHT_DP / 2 + MARGIN_DP),
    )
    entity = Entity.createPanelEntity(
        registrationId,
        Transform(barPose),
        Grabbable(),
        PanelBarComponent(parent, barPose),
    )
  }

  companion object {
    private const val WIDTH_DP = 336
    private const val HEIGHT_DP = 16
    private const val MARGIN_DP = 48
    private const val FADE_IN_DELAY = 500L

    // Conversion constants, do not modify
    private const val PIXELS_TO_METERS = 0.0254f / 100f
    private fun dpToPx(dp: Int): Int {
      return ((dp * DEFAULT_DPI).toFloat() / 160f).toInt()
    }

    private fun dpToMeters(dp: Int): Float {
      return dpToPx(dp) * PIXELS_TO_METERS
    }
  }
}
