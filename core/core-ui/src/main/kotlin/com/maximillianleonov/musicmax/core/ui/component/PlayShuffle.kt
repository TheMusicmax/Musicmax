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

package com.maximillianleonov.musicmax.core.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.maximillianleonov.musicmax.core.designsystem.icon.MusicmaxIcons
import com.maximillianleonov.musicmax.core.designsystem.theme.spacing
import com.maximillianleonov.musicmax.core.media.common.R

@Composable
fun PlayShuffleButtons(
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        PlayShuffleButton(
            modifier = Modifier.weight(1f),
            iconResource = MusicmaxIcons.Play.resourceId,
            textResource = R.string.play,
            onClick = onPlayClick
        )
        PlayShuffleButton(
            modifier = Modifier.weight(1f),
            iconResource = MusicmaxIcons.Shuffle.resourceId,
            textResource = R.string.shuffle,
            onClick = onShuffleClick
        )
    }
}

@Composable
fun PlayOutlinedShuffleButtons(
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        PlayShuffleButton(
            modifier = Modifier.weight(1f),
            iconResource = MusicmaxIcons.Play.resourceId,
            textResource = R.string.play,
            onClick = onPlayClick
        )
        OutlinedPlayShuffleButton(
            modifier = Modifier.weight(1f),
            iconResource = MusicmaxIcons.Shuffle.resourceId,
            textResource = R.string.shuffle,
            onClick = onShuffleClick
        )
    }
}

@Composable
private fun PlayShuffleButton(
    @DrawableRes iconResource: Int,
    @StringRes textResource: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(modifier = modifier, onClick = onClick) {
        Icon(
            modifier = Modifier.size(IconSize),
            painter = painterResource(id = iconResource),
            contentDescription = stringResource(id = textResource)
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
        Text(text = stringResource(id = textResource))
    }
}

@Composable
private fun OutlinedPlayShuffleButton(
    @DrawableRes iconResource: Int,
    @StringRes textResource: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(modifier = modifier, onClick = onClick) {
        Icon(
            modifier = Modifier.size(IconSize),
            painter = painterResource(id = iconResource),
            contentDescription = stringResource(id = textResource)
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
        Text(text = stringResource(id = textResource))
    }
}

private val IconSize = 20.dp
