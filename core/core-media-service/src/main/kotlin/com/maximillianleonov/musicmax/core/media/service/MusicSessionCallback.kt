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

import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.maximillianleonov.musicmax.core.common.dispatcher.Dispatcher
import com.maximillianleonov.musicmax.core.common.dispatcher.MusicmaxDispatchers.MAIN
import com.maximillianleonov.musicmax.core.media.notification.common.MusicCommands.FAVORITE_OFF
import com.maximillianleonov.musicmax.core.media.notification.common.MusicCommands.FAVORITE_ON
import com.maximillianleonov.musicmax.core.media.notification.common.MusicCommands.PLAYBACK_MODE_REPEAT
import com.maximillianleonov.musicmax.core.media.notification.common.MusicCommands.PLAYBACK_MODE_REPEAT_ONE
import com.maximillianleonov.musicmax.core.media.notification.common.MusicCommands.PLAYBACK_MODE_SHUFFLE
import com.maximillianleonov.musicmax.core.model.PlaybackMode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import javax.inject.Inject

class MusicSessionCallback @Inject constructor(
    @Dispatcher(MAIN) mainDispatcher: CoroutineDispatcher,
    private val musicActionHandler: MusicActionHandler
) : MediaLibrarySession.Callback {
    private val coroutineScope = CoroutineScope(mainDispatcher + SupervisorJob())
    val customLayout: List<CommandButton> get() = musicActionHandler.customLayout

    fun setPlaybackModeAction(playbackMode: PlaybackMode) {
        val actionsMap = mapOf(
            PlaybackMode.REPEAT to PLAYBACK_MODE_REPEAT,
            PlaybackMode.REPEAT_ONE to PLAYBACK_MODE_REPEAT_ONE,
            PlaybackMode.SHUFFLE to PLAYBACK_MODE_SHUFFLE
        )
        musicActionHandler.setRepeatShuffleCommand(actionsMap.getValue(playbackMode))
    }

    fun toggleFavoriteAction(isFavorite: Boolean) =
        musicActionHandler.setFavoriteCommand(if (isFavorite) FAVORITE_ON else FAVORITE_OFF)

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: List<MediaItem>
    ): ListenableFuture<List<MediaItem>> = Futures.immediateFuture(
        mediaItems.map { mediaItem ->
            mediaItem.buildUpon()
                .setUri(mediaItem.requestMetadata.mediaUri)
                .build()
        }
    )

    override fun onConnect(
        session: MediaSession,
        controller: MediaSession.ControllerInfo
    ): MediaSession.ConnectionResult {
        val connectionResult = super.onConnect(session, controller)
        val availableSessionCommands = connectionResult.availableSessionCommands.buildUpon()
        musicActionHandler.customCommands.values.forEach { commandButton ->
            commandButton.sessionCommand?.let(availableSessionCommands::add)
        }

        return MediaSession.ConnectionResult.accept(
            availableSessionCommands.build(),
            connectionResult.availablePlayerCommands
        )
    }

    override fun onPostConnect(session: MediaSession, controller: MediaSession.ControllerInfo) {
        session.setCustomLayout(controller, musicActionHandler.customLayout)
    }

    override fun onCustomCommand(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        customCommand: SessionCommand,
        args: Bundle
    ): ListenableFuture<SessionResult> {
        musicActionHandler.onCustomCommand(mediaSession = session, customCommand = customCommand)
        session.setCustomLayout(musicActionHandler.customLayout)
        return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
    }

    fun cancelCoroutineScope() {
        coroutineScope.cancel()
        musicActionHandler.cancelCoroutineScope()
    }
}
