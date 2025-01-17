// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.panel_bar

import com.meta.spatial.core.Query
import com.meta.spatial.core.SystemBase
import com.meta.spatial.toolkit.Transform

class PanelBarSystem : SystemBase() {
  override fun execute() {

    val q = Query.where { has(PanelBarComponent.id) }

    for (entity in q.eval()) {
      entity.getComponent<PanelBarComponent>().parent.get()?.let { parent ->
        // TODO: Align panel bar with parent entity, do we even need this with TransformParent?
        parent.getComponent<Transform>().transform
      }
    }
  }
}
