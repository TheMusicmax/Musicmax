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

package com.maximillianleonov.musicmax.feature.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maximillianleonov.musicmax.core.domain.usecase.GetSongsUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.GetUserDataUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.SetSortByUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.SetSortOrderUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.ToggleFavoriteSongUseCase
import com.maximillianleonov.musicmax.core.media.common.MediaConstants
import com.maximillianleonov.musicmax.core.media.service.MusicServiceConnection
import com.maximillianleonov.musicmax.core.model.SortBy
import com.maximillianleonov.musicmax.core.model.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    getSongsUseCase: GetSongsUseCase,
    getUserDataUseCase: GetUserDataUseCase,
    private val setSortOrderUseCase: SetSortOrderUseCase,
    private val setSortByUseCase: SetSortByUseCase,
    private val toggleFavoriteSongUseCase: ToggleFavoriteSongUseCase
) : ViewModel() {
    private val songs = getSongsUseCase()
        .map { songs -> songs.asFlow().filter { it.isFavorite }.toList() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    val uiState = combine(songs, getUserDataUseCase()) { songs, userData ->
        FavoriteUiState.Success(
            songs = songs,
            sortOrder = userData.sortOrder,
            sortBy = userData.sortBy
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = FavoriteUiState.Loading
    )

    val musicState = musicServiceConnection.musicState

    fun onChangeSortOrder(sortOrder: SortOrder) =
        viewModelScope.launch { setSortOrderUseCase(sortOrder) }

    fun onChangeSortBy(sortBy: SortBy) = viewModelScope.launch { setSortByUseCase(sortBy) }

    fun play(startIndex: Int = MediaConstants.DEFAULT_INDEX) =
        musicServiceConnection.playSongs(songs = songs.value, startIndex = startIndex)

    fun shuffle() = musicServiceConnection.shuffleSongs(songs = songs.value)

    fun onToggleFavorite(id: String, isFavorite: Boolean) =
        viewModelScope.launch { toggleFavoriteSongUseCase(id, isFavorite) }
}
