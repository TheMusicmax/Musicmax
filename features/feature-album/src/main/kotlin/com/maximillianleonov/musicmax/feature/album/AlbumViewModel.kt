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

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maximillianleonov.musicmax.core.domain.model.AlbumDetailsModel
import com.maximillianleonov.musicmax.core.domain.usecase.GetAlbumDetailsUseCase
import com.maximillianleonov.musicmax.core.media.service.MusicServiceConnection
import com.maximillianleonov.musicmax.core.model.Album
import com.maximillianleonov.musicmax.core.model.AlbumDetails
import com.maximillianleonov.musicmax.core.ui.mapper.asAlbumDetails
import com.maximillianleonov.musicmax.feature.album.navigation.getAlbumId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    getAlbumDetailsUseCase: GetAlbumDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val albumDetails = getAlbumDetailsUseCase(savedStateHandle.getAlbumId())
        .map(AlbumDetailsModel::asAlbumDetails)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = AlbumDetails(
                album = Album(id = 0L, artworkUri = Uri.EMPTY, name = "", artist = ""),
                songs = emptyList()
            )
        )

    fun play(startIndex: Int) {
        musicServiceConnection.playSongs(songs = albumDetails.value.songs, startIndex = startIndex)
    }
}
