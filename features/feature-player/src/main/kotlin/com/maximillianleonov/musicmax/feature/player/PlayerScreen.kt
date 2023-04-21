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

package com.maximillianleonov.musicmax.feature.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maximillianleonov.musicmax.core.model.MusicState
import com.maximillianleonov.musicmax.core.model.PlaybackMode
import com.maximillianleonov.musicmax.feature.player.component.PlayerBackdropArtworkOverlay
import com.maximillianleonov.musicmax.feature.player.component.PlayerMediaButtons
import com.maximillianleonov.musicmax.feature.player.component.PlayerTimeSlider
import com.maximillianleonov.musicmax.feature.player.component.PlayerTitleArtist

@Composable
internal fun PlayerRoute(
    onSetSystemBarsLightIcons: () -> Unit,
    onResetSystemBarsIcons: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val musicState by viewModel.musicState.collectAsStateWithLifecycle()
    val currentPosition by viewModel.currentPosition.collectAsStateWithLifecycle()
    val playbackMode by viewModel.playbackMode.collectAsStateWithLifecycle()
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()

    PlayerScreen(
        modifier = modifier,
        musicState = musicState,
        currentPosition = currentPosition,
        playbackMode = playbackMode,
        isFavorite = isFavorite,
        onSkipTo = viewModel::skipTo,
        onMediaButtonPlaybackModeClick = viewModel::onTogglePlaybackMode,
        onMediaButtonSkipPreviousClick = viewModel::skipPrevious,
        onMediaButtonPlayClick = viewModel::play,
        onMediaButtonPauseClick = viewModel::pause,
        onMediaButtonSkipNextClick = viewModel::skipNext,
        onMediaButtonFavoriteClick = viewModel::onToggleFavorite
    )

    DisposableEffect(onSetSystemBarsLightIcons, onResetSystemBarsIcons) {
        onSetSystemBarsLightIcons()
        onDispose(onResetSystemBarsIcons)
    }
}

@Composable
private fun PlayerScreen(
    musicState: MusicState,
    currentPosition: Long,
    playbackMode: PlaybackMode,
    isFavorite: Boolean,
    onSkipTo: (Float) -> Unit,
    onMediaButtonPlaybackModeClick: () -> Unit,
    onMediaButtonSkipPreviousClick: () -> Unit,
    onMediaButtonPlayClick: () -> Unit,
    onMediaButtonPauseClick: () -> Unit,
    onMediaButtonSkipNextClick: () -> Unit,
    onMediaButtonFavoriteClick: (isFavorite: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    PlayerBackdropArtworkOverlay(
        modifier = modifier,
        artworkUri = musicState.currentSong.artworkUri,
        contentDescription = musicState.currentSong.title
    ) {
        PlayerTitleArtist(
            title = musicState.currentSong.title,
            artist = musicState.currentSong.artist
        )

        PlayerTimeSlider(
            currentPosition = currentPosition,
            duration = musicState.duration,
            onSkipTo = onSkipTo
        )

        PlayerMediaButtons(
            isPlaying = !musicState.playWhenReady,
            playbackMode = playbackMode,
            isFavorite = isFavorite,
            onPlaybackModeClick = onMediaButtonPlaybackModeClick,
            onSkipPreviousClick = onMediaButtonSkipPreviousClick,
            onPlayClick = onMediaButtonPlayClick,
            onPauseClick = onMediaButtonPauseClick,
            onSkipNextClick = onMediaButtonSkipNextClick,
            onToggleFavorite = onMediaButtonFavoriteClick
        )
    }
}
