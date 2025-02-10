// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.panel_bar

import androidx.compose.ui.unit.dp
import com.meta.levinriegner.mediaview.R
import com.meta.spatial.compose.composePanel
import com.meta.spatial.core.Entity
import com.meta.spatial.runtime.LayerConfig
import com.meta.spatial.runtime.PanelConfigOptions.Companion.DEFAULT_DPI
import com.meta.spatial.runtime.PanelSceneObject
import com.meta.spatial.toolkit.Grabbable
import com.meta.spatial.toolkit.PanelRegistration
import com.meta.spatial.toolkit.SceneObjectSystem
import com.meta.spatial.toolkit.SpatialActivityManager
import com.meta.spatial.toolkit.Transform
import com.meta.spatial.toolkit.createPanelEntity
import timber.log.Timber
import kotlin.random.Random

class PanelBar(
  private val parent: Entity,
    // Unique registration id for the panel. Prefer using your own id generator.
  private val registrationId: Int = Random.nextInt(Int.MIN_VALUE, Int.MAX_VALUE),
) {

  private val activity get() = SpatialActivityManager.currentActivity
  private val systemManager get() = activity?.get()?.systemManager
  fun attach() {
    register()
    create()
  }

  private fun register() {
    activity?.get()?.let { activity ->
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
    systemManager?.findSystem<SceneObjectSystem>()?.getSceneObject(parent)?.thenAccept {
      val panel = it as? PanelSceneObject
      if (panel == null) {
        Timber.w("Parent is not a panel")
        return@thenAccept
      }
      val parentHeightMeters = panel.getPanelShapeConfig().height
      barPose.t = barPose.t.copy(
          y = barPose.t.y - parentHeightMeters / 2
              - dpToMeters(HEIGHT_DP / 2 + MARGIN_DP),
      )
      Entity.createPanelEntity(
          registrationId,
          Transform(barPose),
          Grabbable(),
          PanelBarComponent(parent, barPose),
      )
    }
  }

  companion object {
    private const val WIDTH_DP = 336
    private const val HEIGHT_DP = 16
    private const val MARGIN_DP = 48

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
