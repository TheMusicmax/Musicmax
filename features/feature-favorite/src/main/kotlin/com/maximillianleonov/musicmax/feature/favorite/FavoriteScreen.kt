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

package com.maximillianleonov.musicmax.feature.favorite

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maximillianleonov.musicmax.core.designsystem.theme.spacing
import com.maximillianleonov.musicmax.core.model.MusicState
import com.maximillianleonov.musicmax.core.model.SortBy
import com.maximillianleonov.musicmax.core.model.SortOrder
import com.maximillianleonov.musicmax.core.ui.component.OutlinedMediaHeader
import com.maximillianleonov.musicmax.core.ui.component.Songs

@Composable
internal fun FavoriteRoute(
    onNavigateToPlayer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val musicState by viewModel.musicState.collectAsStateWithLifecycle()

    when (val uiState = state) {
        FavoriteUiState.Loading -> Unit
        is FavoriteUiState.Success -> {
            FavoriteScreen(
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
                onToggleFavorite = viewModel::onToggleFavorite
            )
        }
    }
}

@Composable
private fun FavoriteScreen(
    uiState: FavoriteUiState.Success,
    musicState: MusicState,
    onChangeSortOrder: (SortOrder) -> Unit,
    onChangeSortBy: (SortBy) -> Unit,
    onSongClick: (Int) -> Unit,
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit,
    onToggleFavorite: (id: String, isFavorite: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(visible = uiState.songs.isNotEmpty()) {
            OutlinedMediaHeader(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small),
                sortOrder = uiState.sortOrder,
                sortBy = uiState.sortBy,
                onChangeSortOrder = onChangeSortOrder,
                onChangeSortBy = onChangeSortBy,
                onPlayClick = onPlayClick,
                onShuffleClick = onShuffleClick
            )
        }

        Songs(
            songs = uiState.songs,
            currentPlayingSongId = musicState.currentMediaId,
            onClick = onSongClick,
            onToggleFavorite = onToggleFavorite
        )
    }
}
