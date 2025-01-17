// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.panel_bar

import androidx.compose.ui.unit.dp
import com.meta.spatial.compose.composePanel
import com.meta.spatial.core.Entity
import com.meta.spatial.runtime.LayerConfig
import com.meta.spatial.runtime.PanelConfigOptions.Companion.DEFAULT_DPI
import com.meta.spatial.toolkit.AppSystemActivity
import com.meta.spatial.toolkit.Grabbable
import com.meta.spatial.toolkit.PanelRegistration
import com.meta.spatial.toolkit.Transform
import com.meta.spatial.toolkit.TransformParent
import com.meta.spatial.toolkit.createPanelEntity
import java.lang.ref.WeakReference
import kotlin.random.Random

class PanelBar(
  appSystemActivity: AppSystemActivity,
  parent: Entity,
    // Unique registration id for the panel. Prefer using your own id generator.
  private val registrationId: Int = Random.nextInt(Int.MIN_VALUE, Int.MAX_VALUE),
    // TODO: See if we can get this on runtime
  private val widthMeters: Float,
) {

  private val activity = WeakReference(appSystemActivity)
  private val parent = WeakReference(parent)
  private var entity: Entity? = null

  fun attach() {
    register()
    create()
  }

  fun destroy() {
    parent.clear()
    activity.clear()
    entity?.destroy()
    entity = null
  }

  private fun register() {
    activity.get()?.let { activity ->
      activity.registerPanel(
          PanelRegistration(registrationId) { _ ->
            config {
              width = widthMeters
              height = dpToMeters(HEIGHT_DP)
              includeGlass = true
              enableTransparent = true
              layerConfig = LayerConfig()
            }
            composePanel {
              setContent {
                PanelBarView(HEIGHT_DP.dp)
              }
            }
          },
      )
    } ?: PanelBarConfig.logger.log(Logger.Level.Error, "Failed to register panel, activity is null")
  }

  private fun create() {
    parent.get()?.let { parent ->
      entity = Entity.createPanelEntity(
          registrationId,
          Transform(), // TODO: bottom-center
          TransformParent(parent),
          Grabbable(),
          PanelBarComponent(parent)
      )
    } ?: PanelBarConfig.logger.log(Logger.Level.Error, "Failed to register panel, parent is null")
  }

  companion object {
    private const val HEIGHT_DP = 4

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
