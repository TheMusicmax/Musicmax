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

package com.maximillianleonov.musicmax.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maximillianleonov.musicmax.core.domain.model.AlbumModel
import com.maximillianleonov.musicmax.core.domain.model.ArtistModel
import com.maximillianleonov.musicmax.core.domain.model.FolderModel
import com.maximillianleonov.musicmax.core.domain.model.SongModel
import com.maximillianleonov.musicmax.core.domain.usecase.GetAlbumsUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.GetArtistsUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.GetFoldersUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.GetSongsUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.ToggleFavoriteSongUseCase
import com.maximillianleonov.musicmax.core.media.common.MediaConstants
import com.maximillianleonov.musicmax.core.media.service.MusicServiceConnection
import com.maximillianleonov.musicmax.core.ui.mapper.asAlbum
import com.maximillianleonov.musicmax.core.ui.mapper.asArtist
import com.maximillianleonov.musicmax.core.ui.mapper.asFolder
import com.maximillianleonov.musicmax.core.ui.mapper.asSong
import com.maximillianleonov.musicmax.core.ui.mapper.listMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    getSongsUseCase: GetSongsUseCase,
    getArtistsUseCase: GetArtistsUseCase,
    getAlbumsUseCase: GetAlbumsUseCase,
    getFoldersUseCase: GetFoldersUseCase,
    private val toggleFavoriteSongUseCase: ToggleFavoriteSongUseCase
) : ViewModel() {
    val songs = getSongsUseCase()
        .listMap(SongModel::asSong)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    val artists = getArtistsUseCase()
        .listMap(ArtistModel::asArtist)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    val albums = getAlbumsUseCase()
        .listMap(AlbumModel::asAlbum)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    val folders = getFoldersUseCase()
        .listMap(FolderModel::asFolder)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    fun play(startIndex: Int = MediaConstants.DEFAULT_INDEX) =
        musicServiceConnection.playSongs(songs = songs.value, startIndex = startIndex)

    fun shuffle() = musicServiceConnection.shuffleSongs(songs = songs.value)

    fun onToggleFavorite(id: String, isFavorite: Boolean) =
        viewModelScope.launch { toggleFavoriteSongUseCase(id, isFavorite) }
}
