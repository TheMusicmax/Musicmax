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
import com.maximillianleonov.musicmax.core.domain.model.ArtistDetailsModel
import com.maximillianleonov.musicmax.core.domain.usecase.GetArtistDetailsUseCase
import com.maximillianleonov.musicmax.core.media.service.MusicServiceConnection
import com.maximillianleonov.musicmax.core.model.Artist
import com.maximillianleonov.musicmax.core.model.ArtistDetails
import com.maximillianleonov.musicmax.core.ui.mapper.asArtistDetails
import com.maximillianleonov.musicmax.feature.artist.navigation.getArtistId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    getArtistDetailsUseCase: GetArtistDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val artistDetails = getArtistDetailsUseCase(savedStateHandle.getArtistId())
        .map(ArtistDetailsModel::asArtistDetails)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ArtistDetails(
                artist = Artist(id = 0L, name = "", numberOfSongs = 0),
                songs = emptyList()
            )
        )

    fun play(startIndex: Int) {
        musicServiceConnection.playSongs(songs = artistDetails.value.songs, startIndex = startIndex)
    }
}
