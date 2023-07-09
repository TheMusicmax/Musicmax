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

import android.content.Context
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import com.maximillianleonov.musicmax.core.common.dispatcher.Dispatcher
import com.maximillianleonov.musicmax.core.common.dispatcher.MusicmaxDispatchers.MAIN
import com.maximillianleonov.musicmax.core.designsystem.icon.MusicmaxIcons
import com.maximillianleonov.musicmax.core.domain.usecase.SetPlaybackModeUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.ToggleFavoriteSongUseCase
import com.maximillianleonov.musicmax.core.media.notification.common.MusicCommands.FAVORITE
import com.maximillianleonov.musicmax.core.media.notification.common.MusicCommands.FAVORITE_OFF
import com.maximillianleonov.musicmax.core.media.notification.common.MusicCommands.FAVORITE_ON
import com.maximillianleonov.musicmax.core.media.notification.common.MusicCommands.PLAYBACK_MODE
import com.maximillianleonov.musicmax.core.media.notification.common.MusicCommands.PLAYBACK_MODE_REPEAT
import com.maximillianleonov.musicmax.core.media.notification.common.MusicCommands.PLAYBACK_MODE_REPEAT_ONE
import com.maximillianleonov.musicmax.core.media.notification.common.MusicCommands.PLAYBACK_MODE_SHUFFLE
import com.maximillianleonov.musicmax.core.model.PlaybackMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.maximillianleonov.musicmax.core.media.common.R as mediaCommonR

class MusicActionHandler @Inject constructor(
    @Dispatcher(MAIN) mainDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context,
    private val setPlaybackModeUseCase: SetPlaybackModeUseCase,
    private val toggleFavoriteSongUseCase: ToggleFavoriteSongUseCase
) {
    private val coroutineScope = CoroutineScope(mainDispatcher + SupervisorJob())

    val customCommands = getAvailableCustomCommands()
    private val customLayoutMap = mutableMapOf<String, CommandButton>().apply {
        this[PLAYBACK_MODE] = customCommands.getValue(PLAYBACK_MODE_REPEAT)
        this[FAVORITE] = customCommands.getValue(FAVORITE_OFF)
    }
    val customLayout: List<CommandButton> get() = customLayoutMap.values.toList()

    fun onCustomCommand(mediaSession: MediaSession, customCommand: SessionCommand) {
        when (customCommand.customAction) {
            PLAYBACK_MODE_REPEAT, PLAYBACK_MODE_REPEAT_ONE, PLAYBACK_MODE_SHUFFLE -> {
                handleRepeatShuffleCommand(action = customCommand.customAction)
            }

            FAVORITE_ON, FAVORITE_OFF -> {
                val id = mediaSession.player.currentMediaItem?.mediaId ?: return
                handleFavoriteCommand(action = customCommand.customAction, id = id)
            }
        }
    }

    fun setRepeatShuffleCommand(action: String) =
        customLayoutMap.set(PLAYBACK_MODE, customCommands.getValue(action))

    fun setFavoriteCommand(action: String) =
        customLayoutMap.set(FAVORITE, customCommands.getValue(action))

    fun cancelCoroutineScope() = coroutineScope.cancel()

    private fun handleRepeatShuffleCommand(action: String) = coroutineScope.launch {
        when (action) {
            PLAYBACK_MODE_REPEAT -> setPlaybackModeUseCase(PlaybackMode.REPEAT_ONE)
            PLAYBACK_MODE_REPEAT_ONE -> setPlaybackModeUseCase(PlaybackMode.SHUFFLE)
            PLAYBACK_MODE_SHUFFLE -> setPlaybackModeUseCase(PlaybackMode.REPEAT)
        }
    }

    private fun handleFavoriteCommand(action: String, id: String) = coroutineScope.launch {
        when (action) {
            FAVORITE_ON -> toggleFavoriteSongUseCase(id = id, isFavorite = false)
            FAVORITE_OFF -> toggleFavoriteSongUseCase(id = id, isFavorite = true)
        }
    }

    private fun getAvailableCustomCommands() = mapOf(
        PLAYBACK_MODE_REPEAT to buildCustomCommand(
            action = PLAYBACK_MODE_REPEAT,
            displayName = context.getString(mediaCommonR.string.repeat),
            iconResource = MusicmaxIcons.Repeat.resourceId
        ),
        PLAYBACK_MODE_REPEAT_ONE to buildCustomCommand(
            action = PLAYBACK_MODE_REPEAT_ONE,
            displayName = context.getString(mediaCommonR.string.repeat_one),
            iconResource = MusicmaxIcons.RepeatOne.resourceId
        ),
        PLAYBACK_MODE_SHUFFLE to buildCustomCommand(
            action = PLAYBACK_MODE_SHUFFLE,
            displayName = context.getString(mediaCommonR.string.shuffle),
            iconResource = MusicmaxIcons.Shuffle.resourceId
        ),
        FAVORITE_ON to buildCustomCommand(
            action = FAVORITE_ON,
            displayName = context.getString(mediaCommonR.string.favorite_remove),
            iconResource = MusicmaxIcons.FavoriteDrawable.resourceId
        ),
        FAVORITE_OFF to buildCustomCommand(
            action = FAVORITE_OFF,
            displayName = context.getString(mediaCommonR.string.favorite_add),
            iconResource = MusicmaxIcons.FavoriteBorderDrawable.resourceId
        )
    )
}

private fun buildCustomCommand(
    action: String,
    displayName: String,
    @DrawableRes iconResource: Int,
) = CommandButton.Builder()
    .setSessionCommand(SessionCommand(action, Bundle.EMPTY))
    .setDisplayName(displayName)
    .setIconResId(iconResource)
    .build()
