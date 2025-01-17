// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.panel_bar

import com.meta.spatial.core.ComponentBase
import com.meta.spatial.core.Entity
import java.lang.ref.WeakReference

class PanelBarComponent(
  parent: Entity,
) : ComponentBase() {

  val parent: WeakReference<Entity> = WeakReference(parent)

  override fun typeID(): Int = id

  companion object {
    const val id = 10005
  }
}
