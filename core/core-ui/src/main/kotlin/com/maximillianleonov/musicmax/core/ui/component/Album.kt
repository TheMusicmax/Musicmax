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

package com.maximillianleonov.musicmax.core.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.maximillianleonov.musicmax.core.designsystem.component.MusicmaxCard
import com.maximillianleonov.musicmax.core.designsystem.component.SingleLineText
import com.maximillianleonov.musicmax.core.designsystem.theme.spacing
import com.maximillianleonov.musicmax.core.model.Album
import com.maximillianleonov.musicmax.core.ui.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun Albums(
    albums: List<Album>,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (albums.isNotEmpty()) {
        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(),
            columns = GridCells.Fixed(count = ColumnsCount)
        ) {
            items(items = albums, key = Album::id) { album ->
                AlbumItem(
                    modifier = Modifier.animateItemPlacement(),
                    album = album,
                    onClick = { onClick(album.id) }
                )
            }
        }
    } else {
        EmptyContent(textResource = R.string.no_albums)
    }
}

@Composable
private fun AlbumItem(
    album: Album,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MusicmaxCard(modifier = modifier, onClick = onClick) {
        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.smallMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            MusicmaxArtworkImage(
                modifier = Modifier.aspectRatio(1f),
                artworkUri = album.artworkUri,
                contentDescription = album.name
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                SingleLineText(
                    text = album.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                SingleLineText(
                    text = album.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private const val ColumnsCount = 2
