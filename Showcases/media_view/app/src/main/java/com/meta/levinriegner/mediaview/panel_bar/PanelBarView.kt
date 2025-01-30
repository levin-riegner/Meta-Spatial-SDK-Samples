// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.panel_bar

import android.icu.text.ListFormatter.Width
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import timber.log.Timber

private const val UNFOCUSED_RATIO = 0.9f

@Composable
fun PanelBarView(
  width: Dp,
  height: Dp,
) {

  val interactionSource = remember { MutableInteractionSource() }
  var focused by remember { mutableStateOf(false) }
  val animatedSizeRatio by animateFloatAsState(
      targetValue = if (focused) 1f else UNFOCUSED_RATIO,
      animationSpec = tween(150),
      label = "SizeRatioAnimation",
  )
//  LaunchedEffect(interactionSource) {
//    interactionSource.interactions.collect { interaction ->
//      when (interaction) {
//        is HoverInteraction.Enter -> {
//          focused = true
//        }
//        is HoverInteraction.Exit -> {
//          focused = false
//        }
//      }
//    }
//  }
  Box(
      modifier = Modifier
          .fillMaxSize()
          .background(Color.Transparent)
          .hoverable(interactionSource = interactionSource)
          .focusable(interactionSource = interactionSource),
      contentAlignment = Alignment.Center,
  ) {
    Box(
        modifier = Modifier
            .width(width * animatedSizeRatio)
            .height(height * animatedSizeRatio)
            .clip(RoundedCornerShape(height / 2))
            .background(Color.Gray),
    )
  }

}
