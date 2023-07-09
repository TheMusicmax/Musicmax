/*
 * Copyright 2022 Afig Aliyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.maximillianleonov.musicmax.feature.player.mini

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.maximillianleonov.musicmax.core.designsystem.icon.MusicmaxIcons
import com.maximillianleonov.musicmax.core.media.common.R as mediaCommonR

@Composable
internal fun MiniPlayerMediaButtons(
    isPlaying: Boolean,
    onSkipPreviousClick: () -> Unit,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onSkipNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MediaButton(
            iconResource = MusicmaxIcons.SkipPrevious.resourceId,
            contentDescriptionResource = mediaCommonR.string.skip_previous,
            onClick = onSkipPreviousClick
        )

        Crossfade(
            targetState = isPlaying,
            animationSpec = spring(),
            label = "CrossfadeAnimation"
        ) { targetIsPlaying ->
            val iconResource = if (targetIsPlaying) {
                MusicmaxIcons.Play.resourceId
            } else {
                MusicmaxIcons.Pause.resourceId
            }

            val contentDescriptionResource = if (targetIsPlaying) {
                mediaCommonR.string.play
            } else {
                mediaCommonR.string.pause
            }

            val onClick = if (targetIsPlaying) onPlayClick else onPauseClick

            MediaButton(
                iconResource = iconResource,
                contentDescriptionResource = contentDescriptionResource,
                onClick = onClick
            )
        }

        MediaButton(
            iconResource = MusicmaxIcons.SkipNext.resourceId,
            contentDescriptionResource = mediaCommonR.string.skip_next,
            onClick = onSkipNextClick
        )
    }
}

@Composable
private fun MediaButton(
    @DrawableRes iconResource: Int,
    @StringRes contentDescriptionResource: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(
        contentColor = MaterialTheme.colorScheme.primary
    ),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) MediaButtonPressedScale else 1f,
        animationSpec = MediaButtonPressedAnimation,
        label = "ScaleAnimation"
    )
    val alpha by animateFloatAsState(
        targetValue = if (isPressed) MediaButtonPressedAlpha else 1f,
        animationSpec = MediaButtonPressedAnimation,
        label = "AlphaAnimation"
    )

    IconButton(
        modifier = modifier.graphicsLayer(scaleX = scale, scaleY = scale, alpha = alpha),
        onClick = onClick,
        colors = colors,
        interactionSource = interactionSource
    ) {
        Icon(
            painter = painterResource(id = iconResource),
            contentDescription = stringResource(id = contentDescriptionResource)
        )
    }
}

private const val MediaButtonPressedScale = 0.85f
private const val MediaButtonPressedAlpha = 0.75f
private val MediaButtonPressedAnimation = tween<Float>()
