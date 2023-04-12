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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maximillianleonov.musicmax.core.domain.model.AlbumModel
import com.maximillianleonov.musicmax.core.domain.usecase.GetAlbumUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.ToggleFavoriteSongUseCase
import com.maximillianleonov.musicmax.core.media.common.MediaConstants
import com.maximillianleonov.musicmax.core.media.service.MusicServiceConnection
import com.maximillianleonov.musicmax.core.ui.mapper.asAlbum
import com.maximillianleonov.musicmax.feature.album.navigation.getAlbumId
import com.maximillianleonov.musicmax.feature.album.util.EmptyAlbum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    getAlbumUseCase: GetAlbumUseCase,
    private val toggleFavoriteSongUseCase: ToggleFavoriteSongUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val album = getAlbumUseCase(savedStateHandle.getAlbumId())
        .map(AlbumModel::asAlbum)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = EmptyAlbum
        )

    fun play(startIndex: Int = MediaConstants.DEFAULT_INDEX) =
        musicServiceConnection.playSongs(songs = album.value.songs, startIndex = startIndex)

    fun shuffle() = musicServiceConnection.shuffleSongs(songs = album.value.songs)

    fun onToggleFavorite(id: String, isFavorite: Boolean) =
        viewModelScope.launch { toggleFavoriteSongUseCase(id, isFavorite) }
}
