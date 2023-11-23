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

package com.maximillianleonov.musicmax.feature.player.component

import android.net.Uri
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.util.lerp
import coil.request.ImageRequest
import com.maximillianleonov.musicmax.core.designsystem.component.MusicmaxImage
import com.maximillianleonov.musicmax.core.designsystem.component.MusicmaxOverlay
import com.maximillianleonov.musicmax.core.designsystem.theme.spacing
import com.maximillianleonov.musicmax.core.model.Song
import com.maximillianleonov.musicmax.core.ui.component.MusicmaxArtworkImage
import com.maximillianleonov.musicmax.feature.player.util.BlurTransformation
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PlayerBackdropArtworkOverlay(
    playingQueueSongs: List<Song>,
    currentSongIndex: Int,
    currentMediaId: String,
    onSkipToIndex: (Int) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = currentSongIndex,
        pageCount = { playingQueueSongs.size.coerceAtLeast(1) }
    )
    val currentSong = playingQueueSongs.getOrNull(currentSongIndex)

    LaunchedEffect(currentSong, currentMediaId, currentSongIndex) {
        if (currentSong?.mediaId != currentMediaId) return@LaunchedEffect

        if (currentSongIndex != pagerState.currentPage) {
            pagerState.animateScrollToPage(page = currentSongIndex)
        }
    }

    LaunchedEffect(
        currentSong,
        currentMediaId,
        pagerState.currentPage,
        pagerState.isScrollInProgress
    ) {
        if (currentSong?.mediaId != currentMediaId) return@LaunchedEffect

        val currentPage = pagerState.currentPage
        if (currentSongIndex != currentPage && !pagerState.isScrollInProgress) {
            onSkipToIndex(currentPage)
        }
    }

    Box(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.scrim)
            .fillMaxSize()
    ) {
        Crossfade(
            targetState = currentSong?.artworkUri.orEmpty(),
            label = "BackdropArtworkImageAnimation"
        ) { artworkUri ->
            Box {
                PlayerBackdropArtworkImage(
                    artworkUri = artworkUri,
                    contentDescription = currentSong?.title
                )
                MusicmaxOverlay(
                    color = MaterialTheme.colorScheme.scrim,
                    alpha = BackdropArtworkOverlayAlpha
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(vertical = MaterialTheme.spacing.medium)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.extraLarge)
            ) { page ->
                PlayerFrontArtworkImage(
                    modifier = Modifier
                        .graphicsLayer {
                            val pageOffset = pagerState.calculatePageOffset(page)

                            lerp(
                                start = 0.85f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            ).also { scale ->
                                scaleX = scale
                                scaleY = scale
                            }

                            alpha = lerp(
                                start = 0.5f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                        },
                    artworkUri = playingQueueSongs.getOrNull(page)?.artworkUri.orEmpty(),
                    contentDescription = currentSong?.title
                )
            }
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
    contentScale: ContentScale = ContentScale.Crop
) {
    MusicmaxArtworkImage(
        modifier = modifier.aspectRatio(1f),
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

@OptIn(ExperimentalFoundationApi::class)
private fun PagerState.calculatePageOffset(page: Int) =
    ((currentPage - page) + currentPageOffsetFraction).absoluteValue

private fun Uri?.orEmpty() = this ?: Uri.EMPTY

private const val BackdropArtworkOverlayAlpha = 0.2f
