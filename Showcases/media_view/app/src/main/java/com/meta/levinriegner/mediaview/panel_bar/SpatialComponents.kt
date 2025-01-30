// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.panel_bar

import com.meta.spatial.core.ComponentManager
import com.meta.spatial.core.SystemManager

object SpatialComponents {

  fun registerComponents(componentManager: ComponentManager) {
    // Register components
    componentManager.registerComponent<PanelBarComponent>(PanelBarComponent.Companion)
  }

  fun registerSystems(systemManager: SystemManager) {
    systemManager.registerSystem(PanelBarSystem())
  }
}
