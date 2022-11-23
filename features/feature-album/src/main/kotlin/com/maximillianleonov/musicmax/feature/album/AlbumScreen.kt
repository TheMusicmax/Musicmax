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

package com.maximillianleonov.musicmax.feature.album

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maximillianleonov.musicmax.core.designsystem.component.MusicmaxScaffold
import com.maximillianleonov.musicmax.core.model.AlbumDetails
import com.maximillianleonov.musicmax.core.ui.component.SongItem
import com.maximillianleonov.musicmax.feature.album.component.Header

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun AlbumRoute(
    onNavigateToPlayer: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AlbumViewModel = hiltViewModel()
) {
    val albumDetails by viewModel.albumDetails.collectAsStateWithLifecycle()
    AlbumScreen(
        albumDetails = albumDetails,
        onSongClick = { startIndex ->
            viewModel.play(startIndex)
            onNavigateToPlayer()
        },
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AlbumScreen(
    albumDetails: AlbumDetails,
    onSongClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MusicmaxScaffold(
        modifier = modifier,
        titleResource = R.string.album,
        onBackClick = onBackClick
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .consumedWindowInsets(innerPadding)
        ) {
            item {
                Header(
                    modifier = Modifier.aspectRatio(1f),
                    name = albumDetails.album.name,
                    artist = albumDetails.album.artist,
                    artworkUri = albumDetails.album.artworkUri
                )
            }
            itemsIndexed(albumDetails.songs) { index, song ->
                SongItem(song = song, onClick = { onSongClick(index) })
            }
        }
    }
}
