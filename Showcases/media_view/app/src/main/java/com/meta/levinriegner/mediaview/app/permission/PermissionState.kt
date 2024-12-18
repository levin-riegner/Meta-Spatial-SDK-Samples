// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.permission

sealed class PermissionState {
  // Storage Permission
  data object CheckPermissionState : PermissionState()

  data object RequestPermission : PermissionState()

  data object PermissionDenied : PermissionState()

  data object PermissionAccepted : PermissionState()
}
