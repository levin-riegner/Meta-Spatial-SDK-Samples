// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.panel_bar

object PanelBarConfig {
  lateinit var logger: Logger

  init {
    configure()
  }

  fun configure(logger: Logger = DebugLogger()) {
    this.logger = logger
  }

}
