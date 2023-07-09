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

package com.maximillianleonov.musicmax.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maximillianleonov.musicmax.core.model.MusicState
import com.maximillianleonov.musicmax.core.model.SortBy
import com.maximillianleonov.musicmax.core.model.SortOrder
import com.maximillianleonov.musicmax.core.ui.component.MediaPager

@Composable
internal fun HomeRoute(
    onNavigateToPlayer: () -> Unit,
    onNavigateToArtist: (Long) -> Unit,
    onNavigateToAlbum: (Long) -> Unit,
    onNavigateToFolder: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val musicState by viewModel.musicState.collectAsStateWithLifecycle()

    when (val uiState = state) {
        HomeUiState.Loading -> Unit
        is HomeUiState.Success -> {
            HomeScreen(
                modifier = modifier,
                uiState = uiState,
                musicState = musicState,
                onChangeSortOrder = viewModel::onChangeSortOrder,
                onChangeSortBy = viewModel::onChangeSortBy,
                onSongClick = { startIndex ->
                    viewModel.play(startIndex)
                    onNavigateToPlayer()
                },
                onPlayClick = {
                    viewModel.play()
                    onNavigateToPlayer()
                },
                onShuffleClick = {
                    viewModel.shuffle()
                    onNavigateToPlayer()
                },
                onArtistClick = onNavigateToArtist,
                onAlbumClick = onNavigateToAlbum,
                onFolderClick = onNavigateToFolder,
                onToggleFavorite = viewModel::onToggleFavorite
            )
        }
    }
}

@Composable
private fun HomeScreen(
    uiState: HomeUiState.Success,
    musicState: MusicState,
    onChangeSortOrder: (SortOrder) -> Unit,
    onChangeSortBy: (SortBy) -> Unit,
    onSongClick: (Int) -> Unit,
    onArtistClick: (Long) -> Unit,
    onAlbumClick: (Long) -> Unit,
    onFolderClick: (String) -> Unit,
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit,
    onToggleFavorite: (id: String, isFavorite: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    MediaPager(
        modifier = modifier,
        songs = uiState.songs,
        currentPlayingSongId = musicState.currentMediaId,
        artists = uiState.artists,
        albums = uiState.albums,
        folders = uiState.folders,
        sortOrder = uiState.sortOrder,
        sortBy = uiState.sortBy,
        onChangeSortOrder = onChangeSortOrder,
        onChangeSortBy = onChangeSortBy,
        onSongClick = onSongClick,
        onArtistClick = onArtistClick,
        onAlbumClick = onAlbumClick,
        onFolderClick = onFolderClick,
        onPlayClick = onPlayClick,
        onShuffleClick = onShuffleClick,
        onToggleFavorite = onToggleFavorite
    )
}
