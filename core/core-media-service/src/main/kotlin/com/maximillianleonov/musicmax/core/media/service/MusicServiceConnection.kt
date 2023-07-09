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

package com.maximillianleonov.musicmax.core.media.service

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.Player
import androidx.media3.common.Player.EVENT_MEDIA_ITEM_TRANSITION
import androidx.media3.common.Player.EVENT_MEDIA_METADATA_CHANGED
import androidx.media3.common.Player.EVENT_PLAYBACK_STATE_CHANGED
import androidx.media3.common.Player.EVENT_PLAY_WHEN_READY_CHANGED
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.maximillianleonov.musicmax.core.common.dispatcher.Dispatcher
import com.maximillianleonov.musicmax.core.common.dispatcher.MusicmaxDispatchers.MAIN
import com.maximillianleonov.musicmax.core.domain.usecase.GetPlayingQueueIdsUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.GetPlayingQueueIndexUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.GetSongsUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.SetPlayingQueueIdsUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.SetPlayingQueueIndexUseCase
import com.maximillianleonov.musicmax.core.media.common.MediaConstants.DEFAULT_INDEX
import com.maximillianleonov.musicmax.core.media.common.MediaConstants.DEFAULT_POSITION_MS
import com.maximillianleonov.musicmax.core.media.service.mapper.asMediaItem
import com.maximillianleonov.musicmax.core.media.service.util.asPlaybackState
import com.maximillianleonov.musicmax.core.media.service.util.orDefaultTimestamp
import com.maximillianleonov.musicmax.core.model.MusicState
import com.maximillianleonov.musicmax.core.model.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

@Suppress("TooManyFunctions")
@Singleton
class MusicServiceConnection @Inject constructor(
    @ApplicationContext context: Context,
    @Dispatcher(MAIN) mainDispatcher: CoroutineDispatcher,
    private val getSongsUseCase: GetSongsUseCase,
    private val getPlayingQueueIdsUseCase: GetPlayingQueueIdsUseCase,
    private val setPlayingQueueIdsUseCase: SetPlayingQueueIdsUseCase,
    private val getPlayingQueueIndexUseCase: GetPlayingQueueIndexUseCase,
    private val setPlayingQueueIndexUseCase: SetPlayingQueueIndexUseCase
) {
    private var mediaController: MediaController? = null
    private val coroutineScope = CoroutineScope(mainDispatcher + SupervisorJob())

    private val _musicState = MutableStateFlow(MusicState())
    val musicState = _musicState.asStateFlow()

    val currentPosition = flow {
        while (currentCoroutineContext().isActive) {
            val currentPosition = mediaController?.currentPosition ?: DEFAULT_POSITION_MS
            emit(currentPosition)
            delay(1.milliseconds)
        }
    }

    init {
        coroutineScope.launch {
            mediaController = MediaController.Builder(
                context,
                SessionToken(context, ComponentName(context, MusicService::class.java))
            ).buildAsync().await().apply { addListener(PlayerListener()) }
            updatePlayingQueue()
        }
    }

    fun skipPrevious() = mediaController?.run {
        seekToPrevious()
        play()
    }

    fun play() = mediaController?.play()
    fun pause() = mediaController?.pause()

    fun skipNext() = mediaController?.run {
        seekToNext()
        play()
    }

    fun skipTo(position: Long) = mediaController?.run {
        seekTo(position)
        play()
    }

    fun skipToIndex(index: Int, position: Long = DEFAULT_POSITION_MS) = mediaController?.run {
        seekTo(index, position)
        play()
    }

    fun playSongs(
        songs: List<Song>,
        startIndex: Int = DEFAULT_INDEX,
        startPositionMs: Long = DEFAULT_POSITION_MS
    ) {
        mediaController?.run {
            setMediaItems(songs.map(Song::asMediaItem), startIndex, startPositionMs)
            prepare()
            play()
        }
        coroutineScope.launch { setPlayingQueueIdsUseCase(playingQueueIds = songs.map(Song::mediaId)) }
    }

    fun shuffleSongs(
        songs: List<Song>,
        startIndex: Int = DEFAULT_INDEX,
        startPositionMs: Long = DEFAULT_POSITION_MS
    ) = playSongs(
        songs = songs.shuffled(),
        startIndex = startIndex,
        startPositionMs = startPositionMs
    )

    private inner class PlayerListener : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) {
            if (events.containsAny(
                    EVENT_PLAYBACK_STATE_CHANGED,
                    EVENT_MEDIA_METADATA_CHANGED,
                    EVENT_PLAY_WHEN_READY_CHANGED
                )
            ) {
                updateMusicState(player)
            }

            if (events.contains(EVENT_MEDIA_ITEM_TRANSITION)) {
                updatePlayingQueueIndex(player)
            }
        }
    }

    private fun updateMusicState(player: Player) = with(player) {
        _musicState.update {
            it.copy(
                currentMediaId = currentMediaItem?.mediaId.orEmpty(),
                playbackState = playbackState.asPlaybackState(),
                playWhenReady = playWhenReady,
                duration = duration.orDefaultTimestamp()
            )
        }
    }

    private suspend fun updatePlayingQueue(startPositionMs: Long = DEFAULT_POSITION_MS) {
        val songs = getSongsUseCase().first()
        if (songs.isEmpty()) return

        val playingQueueSongs = getPlayingQueueIdsUseCase().first().mapNotNull { playingQueueId ->
            songs.find { it.mediaId == playingQueueId }
        }.ifEmpty {
            setPlayingQueueIdsUseCase(playingQueueIds = songs.map(Song::mediaId))
            songs
        }

        val startIndex = getPlayingQueueIndexUseCase().first().let { startIndex ->
            if (startIndex < playingQueueSongs.size) {
                startIndex
            } else {
                setPlayingQueueIndexUseCase(index = 0)
                0
            }
        }

        mediaController?.run {
            setMediaItems(playingQueueSongs.map(Song::asMediaItem), startIndex, startPositionMs)
            prepare()
        }
    }

    private fun updatePlayingQueueIndex(player: Player) {
        val index = player.currentMediaItemIndex
        _musicState.update { it.copy(currentSongIndex = index) }
        coroutineScope.launch { setPlayingQueueIndexUseCase(index) }
    }
}
