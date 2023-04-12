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

package com.maximillianleonov.musicmax.feature.artist

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maximillianleonov.musicmax.core.designsystem.component.MusicmaxScaffold
import com.maximillianleonov.musicmax.core.model.Artist
import com.maximillianleonov.musicmax.core.ui.component.SongItem
import com.maximillianleonov.musicmax.feature.artist.component.Header

@Composable
internal fun ArtistRoute(
    onNavigateToPlayer: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ArtistViewModel = hiltViewModel()
) {
    val artist by viewModel.artist.collectAsStateWithLifecycle()
    ArtistScreen(
        modifier = modifier,
        artist = artist,
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
private fun ArtistScreen(
    artist: Artist,
    onSongClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit,
    onToggleFavorite: (id: String, isFavorite: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    MusicmaxScaffold(
        modifier = modifier,
        titleResource = R.string.artist,
        onBackClick = onBackClick
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            item {
                Header(
                    name = artist.name,
                    numberOfSongs = artist.songs.size,
                    onPlayClick = onPlayClick,
                    onShuffleClick = onShuffleClick
                )
            }
            itemsIndexed(artist.songs) { index, song ->
                SongItem(
                    song = song,
                    onClick = { onSongClick(index) },
                    onToggleFavorite = { isFavorite -> onToggleFavorite(song.mediaId, isFavorite) }
                )
            }
        }
    }
}
