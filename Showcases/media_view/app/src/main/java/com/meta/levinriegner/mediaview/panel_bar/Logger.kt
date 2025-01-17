// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.panel_bar

interface Logger {

  fun log(level: Level, message: String)

  enum class Level {
    Info, Error,
  }
}

class DebugLogger : Logger {

  override fun log(level: Logger.Level, message: String) {
    println("PanelBar [$level] $message")
  }

}
