// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.panel_bar

import androidx.compose.ui.res.painterResource
import com.meta.spatial.core.Pose
import com.meta.spatial.core.Quaternion
import com.meta.spatial.core.Query
import com.meta.spatial.core.SystemBase
import com.meta.spatial.core.Vector3
import com.meta.spatial.toolkit.Panel
import com.meta.spatial.toolkit.PlayerBodyAttachmentSystem
import com.meta.spatial.toolkit.Transform
import timber.log.Timber

class PanelBarSystem : SystemBase() {
  override fun execute() {

    val q = Query.where { has(PanelBarComponent.id) and changed(Transform.id) }
    for (entity in q.eval()) {
      val panelBarComponent = entity.getComponent<PanelBarComponent>()
      val parent = panelBarComponent.parent
      val parentTransform = parent.tryGetComponent<Transform>()
      if (parentTransform == null) {
        // TODO: This doesn't work (code is never reached)
        Timber.d("Parent is destroyed")
        entity.destroy()
        return
      }
      Timber.i("Panel bar has parent")
      // Check is alive
      val currentPose = entity.getComponent<Transform>().transform
      val previousPose = panelBarComponent.pose
      Timber.i("Current pose: $currentPose")
      Timber.i("Previous pose: $previousPose")
      if (currentPose != previousPose) {
        // Calculate difference
        val diff = currentPose.t - previousPose.t
        val qDiff = currentPose.q * previousPose.q.inverse()
        Timber.i("Panel bar moved by $diff and rotated by $qDiff")
        // Apply difference to parent
        val parentPose = parentTransform.transform
        val newParentPose = parentPose.copy(
            t = parentPose.t + diff,
            q = parentPose.q * qDiff,
        )
        parent.setComponent(Transform(newParentPose))
        // Update component
        entity.setComponent(panelBarComponent.copy(pose = currentPose))
        Timber.i("Panel bar moved to parent")
      }
    }
  }

  private fun getHeadPose(): Pose? {
    val head =
        systemManager
            .tryFindSystem<PlayerBodyAttachmentSystem>()
            ?.tryGetLocalPlayerAvatarBody()
            ?.head

    val headPose = head?.tryGetComponent<Transform>()?.transform
    if (headPose == null || headPose == Pose()) return null
    return headPose
  }
}
