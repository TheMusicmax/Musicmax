/*
 * Copyright 2022 Maximillian Leonov
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

package com.maximillianleonov.musicmax.feature.player.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.maximillianleonov.musicmax.core.designsystem.icon.MusicmaxIcons
import com.maximillianleonov.musicmax.core.designsystem.theme.spacing
import com.maximillianleonov.musicmax.core.media.common.R

@Composable
internal fun PlayerMediaButtons(
    playWhenReady: Boolean,
    onSkipPreviousClick: () -> Unit,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onSkipNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MediaButton(
            iconResource = MusicmaxIcons.SkipPrevious.resourceId,
            contentDescriptionResource = R.string.skip_previous,
            onClick = onSkipPreviousClick
        )

        Crossfade(targetState = playWhenReady, animationSpec = spring()) { targetPlayWhenReady ->
            PlayPauseMediaButton(
                playWhenReady = targetPlayWhenReady,
                playIconResource = MusicmaxIcons.Play.resourceId,
                playContentDescriptionResource = R.string.play,
                onPlayClick = onPlayClick,
                pauseIconResource = MusicmaxIcons.Pause.resourceId,
                pauseContentDescriptionResource = R.string.pause,
                onPauseClick = onPauseClick
            )
        }

        MediaButton(
            iconResource = MusicmaxIcons.SkipNext.resourceId,
            contentDescriptionResource = R.string.skip_next,
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
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(contentColor = MediaButtonIconColor),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) MediaButtonPressedScale else 1f,
        animationSpec = MediaButtonPressedAnimation
    )
    val alpha by animateFloatAsState(
        targetValue = if (isPressed) MediaButtonPressedAlpha else 1f,
        animationSpec = MediaButtonPressedAnimation
    )

    IconButton(
        modifier = modifier.graphicsLayer(scaleX = scale, scaleY = scale, alpha = alpha),
        onClick = onClick,
        colors = colors,
        interactionSource = interactionSource
    ) {
        Icon(
            modifier = Modifier.size(MediaButtonIconSize),
            painter = painterResource(id = iconResource),
            contentDescription = stringResource(id = contentDescriptionResource)
        )
    }
}

@Composable
private fun PlayPauseMediaButton(
    playWhenReady: Boolean,
    @DrawableRes playIconResource: Int,
    @StringRes playContentDescriptionResource: Int,
    onPlayClick: () -> Unit,
    @DrawableRes pauseIconResource: Int,
    @StringRes pauseContentDescriptionResource: Int,
    onPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    color: Color = MediaButtonIconColor,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication = LocalIndication.current
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) MediaButtonPressedScale else 1f,
        animationSpec = MediaButtonPressedAnimation
    )
    val alpha by animateFloatAsState(
        targetValue = if (isPressed) MediaButtonPressedAlpha else 1f,
        animationSpec = MediaButtonPressedAnimation
    )

    Box(
        modifier = modifier
            .graphicsLayer(scaleX = scale, scaleY = scale, alpha = alpha)
            .clip(shape = shape)
            .clickable(
                interactionSource = interactionSource,
                indication = indication,
                onClick = if (playWhenReady) onPauseClick else onPlayClick
            )
    ) {
        Icon(
            modifier = Modifier
                .background(
                    brush = SolidColor(MediaButtonBackgroundColor),
                    shape = shape,
                    alpha = MediaButtonBackgroundAlpha
                )
                .padding(MaterialTheme.spacing.medium)
                .size(MediaButtonIconSize),
            painter = painterResource(
                id = if (playWhenReady) pauseIconResource else playIconResource
            ),
            contentDescription = stringResource(
                id = if (playWhenReady) {
                    pauseContentDescriptionResource
                } else {
                    playContentDescriptionResource
                }
            ),
            tint = color
        )
    }
}

private const val MediaButtonPressedScale = 0.85f
private const val MediaButtonPressedAlpha = 0.75f
private val MediaButtonPressedAnimation = tween<Float>()
private val MediaButtonIconSize = 32.dp
private val MediaButtonIconColor = Color.White
private val MediaButtonBackgroundColor = Color.Gray
private const val MediaButtonBackgroundAlpha = 0.5f
