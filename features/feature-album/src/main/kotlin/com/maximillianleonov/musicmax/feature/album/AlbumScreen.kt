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
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maximillianleonov.musicmax.core.designsystem.component.MusicmaxScaffold
import com.maximillianleonov.musicmax.core.model.Album
import com.maximillianleonov.musicmax.core.ui.component.songs
import com.maximillianleonov.musicmax.feature.album.component.Header

@Composable
internal fun AlbumRoute(
    onNavigateToPlayer: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AlbumViewModel = hiltViewModel()
) {
    val album by viewModel.album.collectAsStateWithLifecycle()
    AlbumScreen(
        modifier = modifier,
        album = album,
        onSongClick = { startIndex ->
            viewModel.play(startIndex)
            onNavigateToPlayer()
        },
        onBackClick = onBackClick,
        onPlayClick = {
            viewModel.play()
            onNavigateToPlayer()
        },
        onShuffleClick = {
            viewModel.shuffle()
            onNavigateToPlayer()
        },
        onToggleFavorite = viewModel::onToggleFavorite
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AlbumScreen(
    album: Album,
    onSongClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit,
    onToggleFavorite: (id: String, isFavorite: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    MusicmaxScaffold(
        modifier = modifier.fillMaxSize(),
        titleResource = R.string.album,
        onBackClick = onBackClick
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .fillMaxSize()
        ) {
            item {
                Header(
                    modifier = Modifier.aspectRatio(1f),
                    name = album.name,
                    artist = album.artist,
                    artworkUri = album.artworkUri,
                    onPlayClick = onPlayClick,
                    onShuffleClick = onShuffleClick
                )
            }

            songs(
                songs = album.songs,
                onClick = onSongClick,
                onToggleFavorite = onToggleFavorite
            )
        }
    }
}
