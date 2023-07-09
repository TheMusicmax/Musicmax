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

package com.maximillianleonov.musicmax.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maximillianleonov.musicmax.core.domain.usecase.GetUserDataUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.SearchMediaUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.SetSortByUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.SetSortOrderUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.ToggleFavoriteSongUseCase
import com.maximillianleonov.musicmax.core.media.common.MediaConstants
import com.maximillianleonov.musicmax.core.media.service.MusicServiceConnection
import com.maximillianleonov.musicmax.core.model.SearchDetails
import com.maximillianleonov.musicmax.core.model.SortBy
import com.maximillianleonov.musicmax.core.model.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    searchMediaUseCase: SearchMediaUseCase,
    getUserDataUseCase: GetUserDataUseCase,
    private val setSortOrderUseCase: SetSortOrderUseCase,
    private val setSortByUseCase: SetSortByUseCase,
    private val toggleFavoriteSongUseCase: ToggleFavoriteSongUseCase
) : ViewModel() {
    private val query = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    private val searchDetails = query
        .flatMapLatest { query -> searchMediaUseCase(query.trim()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = SearchDetails(
                songs = emptyList(),
                artists = emptyList(),
                albums = emptyList(),
                folders = emptyList()
            )
        )

    val uiState = combine(
        query,
        searchDetails,
        getUserDataUseCase()
    ) { query, searchDetails, userData ->
        SearchUiState.Success(
            query = query,
            searchDetails = searchDetails,
            sortOrder = userData.sortOrder,
            sortBy = userData.sortBy
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = SearchUiState.Loading
    )

    val musicState = musicServiceConnection.musicState

    fun changeQuery(newQuery: String) = query.update { newQuery }

    fun onChangeSortOrder(sortOrder: SortOrder) =
        viewModelScope.launch { setSortOrderUseCase(sortOrder) }

    fun onChangeSortBy(sortBy: SortBy) = viewModelScope.launch { setSortByUseCase(sortBy) }

    fun play(startIndex: Int = MediaConstants.DEFAULT_INDEX) =
        musicServiceConnection.playSongs(songs = searchDetails.value.songs, startIndex = startIndex)

    fun shuffle() = musicServiceConnection.shuffleSongs(songs = searchDetails.value.songs)

    fun onToggleFavorite(id: String, isFavorite: Boolean) =
        viewModelScope.launch { toggleFavoriteSongUseCase(id, isFavorite) }
}
