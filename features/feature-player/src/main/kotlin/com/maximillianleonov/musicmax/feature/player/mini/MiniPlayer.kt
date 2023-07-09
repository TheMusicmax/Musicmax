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

package com.maximillianleonov.musicmax.feature.player.mini

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maximillianleonov.musicmax.core.designsystem.theme.spacing
import com.maximillianleonov.musicmax.core.model.MusicState
import com.maximillianleonov.musicmax.core.model.Song
import com.maximillianleonov.musicmax.feature.player.PlayerViewModel

@Composable
fun MiniPlayer(
    onNavigateToPlayer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val musicState by viewModel.musicState.collectAsStateWithLifecycle()
    val playingQueueSongs by viewModel.playingQueueSongs.collectAsStateWithLifecycle()
    val currentPosition by viewModel.currentPosition.collectAsStateWithLifecycle()
    val currentSong = playingQueueSongs.getOrNull(musicState.currentSongIndex)
        ?.takeIf { it.mediaId == musicState.currentMediaId }

    MiniPlayer(
        modifier = modifier,
        musicState = musicState,
        currentSong = currentSong,
        currentPosition = currentPosition,
        onMediaButtonSkipPreviousClick = viewModel::skipPrevious,
        onMediaButtonPlayClick = viewModel::play,
        onMediaButtonPauseClick = viewModel::pause,
        onMediaButtonSkipNextClick = viewModel::skipNext,
        onNavigateToPlayer = onNavigateToPlayer
    )
}

@Composable
private fun MiniPlayer(
    musicState: MusicState,
    currentSong: Song?,
    currentPosition: Long,
    onMediaButtonSkipPreviousClick: () -> Unit,
    onMediaButtonPlayClick: () -> Unit,
    onMediaButtonPauseClick: () -> Unit,
    onMediaButtonSkipNextClick: () -> Unit,
    onNavigateToPlayer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surfaceColorAtElevation(MiniPlayerStartElevation),
                        MaterialTheme.colorScheme.surfaceColorAtElevation(MiniPlayerEndElevation)
                    )
                )
            )
            .clickable(onClick = onNavigateToPlayer)
    ) {
        Row(
            modifier = Modifier
                .padding(MaterialTheme.spacing.extraSmall)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                MiniPlayerArtworkImage(
                    artworkUri = currentSong?.artworkUri.orEmpty(),
                    contentDescription = currentSong?.title
                )
                MiniPlayerTitleArtist(
                    title = currentSong?.title.orEmpty(),
                    artist = currentSong?.artist.orEmpty()
                )
            }
            MiniPlayerMediaButtons(
                isPlaying = !musicState.playWhenReady,
                onSkipPreviousClick = onMediaButtonSkipPreviousClick,
                onPlayClick = onMediaButtonPlayClick,
                onPauseClick = onMediaButtonPauseClick,
                onSkipNextClick = onMediaButtonSkipNextClick
            )
        }
        MiniPlayerTimeProgress(
            playbackState = musicState.playbackState,
            currentPosition = currentPosition,
            duration = musicState.duration
        )
    }
}

private fun Uri?.orEmpty() = this ?: Uri.EMPTY

private val MiniPlayerStartElevation = 1.dp
private val MiniPlayerEndElevation = 3.dp
