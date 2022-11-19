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

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.request.ImageRequest
import com.maximillianleonov.musicmax.core.designsystem.component.MusicmaxImage
import com.maximillianleonov.musicmax.core.designsystem.component.MusicmaxOverlay
import com.maximillianleonov.musicmax.core.designsystem.theme.spacing
import com.maximillianleonov.musicmax.core.ui.component.MusicmaxArtworkImage
import com.maximillianleonov.musicmax.feature.player.util.BlurTransformation

@Composable
internal fun PlayerBackdropArtworkOverlay(
    artworkUri: Uri,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Box {
            PlayerBackdropArtworkImage(
                artworkUri = artworkUri,
                contentDescription = contentDescription
            )
            MusicmaxOverlay(
                color = MaterialTheme.colorScheme.scrim,
                alpha = BackdropArtworkOverlayAlpha
            )
        }
        Column(
            modifier = Modifier
                .padding(vertical = MaterialTheme.spacing.medium)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            PlayerFrontArtworkImage(
                artworkUri = artworkUri,
                contentDescription = contentDescription
            )
            content()
        }
    }
}

@Composable
private fun PlayerFrontArtworkImage(
    artworkUri: Uri,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    colors: CardColors = CardDefaults.cardColors(containerColor = Color.Transparent),
    contentScale: ContentScale = ContentScale.Fit
) {
    MusicmaxArtworkImage(
        modifier = modifier
            .padding(horizontal = MaterialTheme.spacing.large)
            .aspectRatio(1f),
        artworkUri = artworkUri,
        contentDescription = contentDescription,
        shape = shape,
        colors = colors,
        contentScale = contentScale
    )
}

@Composable
private fun PlayerBackdropArtworkImage(
    artworkUri: Uri,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    colors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
    contentScale: ContentScale = ContentScale.Crop
) {
    MusicmaxImage(
        modifier = modifier.fillMaxSize(),
        model = ImageRequest.Builder(LocalContext.current)
            .data(artworkUri)
            .transformations(BlurTransformation())
            .build(),
        contentDescription = contentDescription,
        shape = shape,
        colors = colors,
        contentScale = contentScale
    )
}

private const val BackdropArtworkOverlayAlpha = 0.2f
