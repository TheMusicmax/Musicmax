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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maximillianleonov.musicmax.core.domain.model.ArtistModel
import com.maximillianleonov.musicmax.core.domain.usecase.GetArtistUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.ToggleFavoriteSongUseCase
import com.maximillianleonov.musicmax.core.media.common.MediaConstants
import com.maximillianleonov.musicmax.core.media.service.MusicServiceConnection
import com.maximillianleonov.musicmax.core.ui.mapper.asArtist
import com.maximillianleonov.musicmax.feature.artist.navigation.getArtistId
import com.maximillianleonov.musicmax.feature.artist.util.EmptyArtist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    getArtistUseCase: GetArtistUseCase,
    private val toggleFavoriteSongUseCase: ToggleFavoriteSongUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val artist = getArtistUseCase(savedStateHandle.getArtistId())
        .map(ArtistModel::asArtist)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = EmptyArtist
        )

    fun play(startIndex: Int = MediaConstants.DEFAULT_INDEX) =
        musicServiceConnection.playSongs(songs = artist.value.songs, startIndex = startIndex)

    fun shuffle() = musicServiceConnection.shuffleSongs(songs = artist.value.songs)

    fun onToggleFavorite(id: String, isFavorite: Boolean) =
        viewModelScope.launch { toggleFavoriteSongUseCase(id, isFavorite) }
}
