// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.panel_bar

import com.meta.spatial.core.ComponentBase
import com.meta.spatial.core.ComponentCompanion
import com.meta.spatial.core.Entity
import com.meta.spatial.core.EntityAttribute
import com.meta.spatial.core.Pose
import com.meta.spatial.core.PoseAttribute

class PanelBarComponent(
  parent: Entity,
  pose: Pose,
) : ComponentBase() {

  var parent by EntityAttribute(
      "parent",
      PanelBarConstants.PANEL_BAR_COMPONENT_ATTR_ENTITY,
      this,
      parent,
  )
  var pose by PoseAttribute(
      "pose",
      PanelBarConstants.PANEL_BAR_COMPONENT_ATTR_POSE,
      this,
      pose,
  )

  fun copy(
      parent: Entity = this.parent,
      pose: Pose = this.pose,
  ): PanelBarComponent {
    return PanelBarComponent(parent, pose)
  }

  override fun typeID(): Int = id

  companion object : ComponentCompanion {
    override val id = PanelBarConstants.PANEL_BAR_COMPONENT_ID
    override val createDefaultInstance = { PanelBarComponent(Entity.create(), Pose()) }
  }
}
