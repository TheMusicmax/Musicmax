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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maximillianleonov.musicmax.core.domain.usecase.GetAlbumByIdUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.GetArtistByIdUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.GetFolderByNameUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.GetUserDataUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.SetSortByUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.SetSortOrderUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.ToggleFavoriteSongUseCase
import com.maximillianleonov.musicmax.core.media.common.MediaConstants
import com.maximillianleonov.musicmax.core.media.service.MusicServiceConnection
import com.maximillianleonov.musicmax.core.model.SortBy
import com.maximillianleonov.musicmax.core.model.SortOrder
import com.maximillianleonov.musicmax.feature.library.model.LibraryType
import com.maximillianleonov.musicmax.feature.library.navigation.getLibraryId
import com.maximillianleonov.musicmax.feature.library.navigation.getLibraryType
import com.maximillianleonov.musicmax.feature.library.util.getSongs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LibraryViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    getArtistByIdUseCase: GetArtistByIdUseCase,
    getAlbumByIdUseCase: GetAlbumByIdUseCase,
    getFolderByNameUseCase: GetFolderByNameUseCase,
    getUserDataUseCase: GetUserDataUseCase,
    savedStateHandle: SavedStateHandle,
    private val setSortOrderUseCase: SetSortOrderUseCase,
    private val setSortByUseCase: SetSortByUseCase,
    private val toggleFavoriteSongUseCase: ToggleFavoriteSongUseCase
) : ViewModel() {
    val uiState = getInitialUiState(
        getArtistByIdUseCase = getArtistByIdUseCase,
        getAlbumByIdUseCase = getAlbumByIdUseCase,
        getFolderByNameUseCase = getFolderByNameUseCase,
        getUserDataUseCase = getUserDataUseCase,
        savedStateHandle = savedStateHandle
    )

    val musicState = musicServiceConnection.musicState

    fun onChangeSortOrder(sortOrder: SortOrder) =
        viewModelScope.launch { setSortOrderUseCase(sortOrder) }

    fun onChangeSortBy(sortBy: SortBy) = viewModelScope.launch { setSortByUseCase(sortBy) }

    fun play(startIndex: Int = MediaConstants.DEFAULT_INDEX) =
        musicServiceConnection.playSongs(songs = uiState.value.getSongs(), startIndex = startIndex)

    fun shuffle() = musicServiceConnection.shuffleSongs(songs = uiState.value.getSongs())

    fun onToggleFavorite(id: String, isFavorite: Boolean) =
        viewModelScope.launch { toggleFavoriteSongUseCase(id, isFavorite) }

    private fun getInitialUiState(
        getArtistByIdUseCase: GetArtistByIdUseCase,
        getAlbumByIdUseCase: GetAlbumByIdUseCase,
        getFolderByNameUseCase: GetFolderByNameUseCase,
        getUserDataUseCase: GetUserDataUseCase,
        savedStateHandle: SavedStateHandle
    ): StateFlow<LibraryUiState> {
        val libraryId = savedStateHandle.getLibraryId()
        return when (savedStateHandle.getLibraryType()) {
            LibraryType.Artist -> {
                combine(
                    getArtistByIdUseCase(artistId = libraryId.toLong()),
                    getUserDataUseCase()
                ) { artist, userData ->
                    LibraryUiState.ArtistType(
                        artist = artist,
                        sortOrder = userData.sortOrder,
                        sortBy = userData.sortBy
                    )
                }
            }

            LibraryType.Album -> {
                combine(
                    getAlbumByIdUseCase(albumId = libraryId.toLong()),
                    getUserDataUseCase()
                ) { album, userData ->
                    LibraryUiState.AlbumType(
                        album = album,
                        sortOrder = userData.sortOrder,
                        sortBy = userData.sortBy
                    )
                }
            }

            LibraryType.Folder -> {
                combine(
                    getFolderByNameUseCase(name = libraryId),
                    getUserDataUseCase()
                ) { folder, userData ->
                    LibraryUiState.FolderType(
                        folder = folder,
                        sortOrder = userData.sortOrder,
                        sortBy = userData.sortBy
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = LibraryUiState.Loading
        )
    }
}
