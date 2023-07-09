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

package com.maximillianleonov.musicmax.feature.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maximillianleonov.musicmax.core.domain.usecase.GetFavoriteSongIdsUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.GetPlaybackModeUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.GetPlayingQueueSongsUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.SetPlaybackModeUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.ToggleFavoriteSongUseCase
import com.maximillianleonov.musicmax.core.media.common.MediaConstants.DEFAULT_POSITION_MS
import com.maximillianleonov.musicmax.core.media.service.MusicServiceConnection
import com.maximillianleonov.musicmax.core.model.PlaybackMode
import com.maximillianleonov.musicmax.feature.player.util.convertToPosition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    getPlayingQueueSongsUseCase: GetPlayingQueueSongsUseCase,
    getPlaybackModeUseCase: GetPlaybackModeUseCase,
    getFavoriteSongIdsUseCase: GetFavoriteSongIdsUseCase,
    private val setPlaybackModeUseCase: SetPlaybackModeUseCase,
    private val toggleFavoriteSongUseCase: ToggleFavoriteSongUseCase
) : ViewModel() {
    val musicState = musicServiceConnection.musicState

    val playingQueueSongs = getPlayingQueueSongsUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    val currentPosition = musicServiceConnection.currentPosition.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = DEFAULT_POSITION_MS
    )

    val playbackMode = getPlaybackModeUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = PlaybackMode.REPEAT
    )

    val isFavorite = combine(
        musicState,
        getFavoriteSongIdsUseCase()
    ) { musicState, favoriteSongIds ->
        musicState.currentMediaId in favoriteSongIds
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )

    fun skipPrevious() = musicServiceConnection.skipPrevious()
    fun play() = musicServiceConnection.play()
    fun pause() = musicServiceConnection.pause()
    fun skipNext() = musicServiceConnection.skipNext()

    fun skipTo(position: Float) =
        musicServiceConnection.skipTo(convertToPosition(position, musicState.value.duration))

    fun skipToIndex(index: Int) = musicServiceConnection.skipToIndex(index)

    fun onTogglePlaybackMode() = viewModelScope.launch {
        val newPlaybackMode = when (playbackMode.value) {
            PlaybackMode.REPEAT -> PlaybackMode.REPEAT_ONE
            PlaybackMode.REPEAT_ONE -> PlaybackMode.SHUFFLE
            PlaybackMode.SHUFFLE -> PlaybackMode.REPEAT
        }
        setPlaybackModeUseCase(playbackMode = newPlaybackMode)
    }

    fun onToggleFavorite(isFavorite: Boolean) = viewModelScope.launch {
        toggleFavoriteSongUseCase(
            id = musicState.value.currentMediaId,
            isFavorite = isFavorite
        )
    }
}
