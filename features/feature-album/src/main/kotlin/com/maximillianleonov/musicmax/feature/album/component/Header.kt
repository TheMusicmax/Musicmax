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

package com.maximillianleonov.musicmax.feature.album.component

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import com.maximillianleonov.musicmax.core.designsystem.component.MusicmaxOverlay
import com.maximillianleonov.musicmax.core.designsystem.theme.spacing
import com.maximillianleonov.musicmax.core.ui.component.MusicmaxArtworkImage
import com.maximillianleonov.musicmax.core.ui.component.PlayShuffleButtons

@Composable
internal fun Header(
    name: String,
    artist: String,
    artworkUri: Uri,
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Box {
            MusicmaxArtworkImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                artworkUri = artworkUri,
                contentDescription = name,
                shape = RectangleShape
            )
            MusicmaxOverlay(
                color = MaterialTheme.colorScheme.scrim,
                alpha = ArtworkOverlayAlpha
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(MaterialTheme.spacing.small)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium,
                color = ContentColor
            )
            Text(
                text = artist,
                style = MaterialTheme.typography.headlineSmall,
                color = SecondColor
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
            PlayShuffleButtons(onPlayClick = onPlayClick, onShuffleClick = onShuffleClick)
        }
    }
}

private const val ArtworkOverlayAlpha = 0.05f
private val ContentColor = Color.White
private const val SecondColorAlpha = 0.75f
private val SecondColor = ContentColor.copy(SecondColorAlpha)
