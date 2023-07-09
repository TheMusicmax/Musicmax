/*
 * Copyright 2023 Afig Aliyev
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

package com.maximillianleonov.musicmax.feature.library

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maximillianleonov.musicmax.core.model.MusicState
import com.maximillianleonov.musicmax.core.model.SortBy
import com.maximillianleonov.musicmax.core.model.SortOrder
import com.maximillianleonov.musicmax.core.ui.component.songs
import com.maximillianleonov.musicmax.feature.library.component.libraryHeader
import com.maximillianleonov.musicmax.feature.library.util.getSongs

@Composable
internal fun LibraryRoute(
    onNavigateToPlayer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val musicState by viewModel.musicState.collectAsStateWithLifecycle()

    LibraryScreen(
        modifier = modifier,
        uiState = uiState,
        musicState = musicState,
        onChangeSortOrder = viewModel::onChangeSortOrder,
        onChangeSortBy = viewModel::onChangeSortBy,
        onPlayClick = {
            viewModel.play()
            onNavigateToPlayer()
        },
        onShuffleClick = {
            viewModel.shuffle()
            onNavigateToPlayer()
        },
        onSongClick = { startIndex ->
            viewModel.play(startIndex)
            onNavigateToPlayer()
        },
        onToggleFavorite = viewModel::onToggleFavorite
    )
}

@Composable
private fun LibraryScreen(
    uiState: LibraryUiState,
    musicState: MusicState,
    onChangeSortOrder: (SortOrder) -> Unit,
    onChangeSortBy: (SortBy) -> Unit,
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit,
    onSongClick: (Int) -> Unit,
    onToggleFavorite: (id: String, isFavorite: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        libraryHeader(
            uiState = uiState,
            onChangeSortOrder = onChangeSortOrder,
            onChangeSortBy = onChangeSortBy,
            onPlayClick = onPlayClick,
            onShuffleClick = onShuffleClick
        )

        songs(
            songs = uiState.getSongs(),
            currentPlayingSongId = musicState.currentMediaId,
            onClick = onSongClick,
            onToggleFavorite = onToggleFavorite
        )
    }
}
