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

package com.maximillianleonov.musicmax.core.media.service

import android.content.Context
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.media3.common.Player
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import com.maximillianleonov.musicmax.core.common.dispatcher.Dispatcher
import com.maximillianleonov.musicmax.core.common.dispatcher.MusicmaxDispatchers.MAIN
import com.maximillianleonov.musicmax.core.designsystem.icon.MusicmaxIcons
import com.maximillianleonov.musicmax.core.domain.usecase.ToggleFavoriteSongUseCase
import com.maximillianleonov.musicmax.core.media.notification.common.MusicCommands.FAVORITE
import com.maximillianleonov.musicmax.core.media.notification.common.MusicCommands.FAVORITE_OFF
import com.maximillianleonov.musicmax.core.media.notification.common.MusicCommands.FAVORITE_ON
import com.maximillianleonov.musicmax.core.media.notification.common.MusicCommands.REPEAT
import com.maximillianleonov.musicmax.core.media.notification.common.MusicCommands.REPEAT_ONE
import com.maximillianleonov.musicmax.core.media.notification.common.MusicCommands.REPEAT_SHUFFLE
import com.maximillianleonov.musicmax.core.media.notification.common.MusicCommands.SHUFFLE
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
    private val toggleFavoriteSongUseCase: ToggleFavoriteSongUseCase
) {
    private val coroutineScope = CoroutineScope(mainDispatcher + SupervisorJob())

    val customCommands = getAvailableCustomCommands()
    private val customLayoutMap = mutableMapOf<String, CommandButton>().apply {
        this[REPEAT_SHUFFLE] = customCommands.getValue(REPEAT)
        this[FAVORITE] = customCommands.getValue(FAVORITE_OFF)
    }
    val customLayout: List<CommandButton> get() = customLayoutMap.values.toList()

    fun onCustomCommand(mediaSession: MediaSession, customCommand: SessionCommand) {
        when (customCommand.customAction) {
            REPEAT, REPEAT_ONE, SHUFFLE -> {
                handleRepeatShuffleCommand(
                    action = customCommand.customAction,
                    player = mediaSession.player
                )
            }

            FAVORITE_ON, FAVORITE_OFF -> {
                handleFavoriteCommand(
                    action = customCommand.customAction,
                    player = mediaSession.player
                )
            }
        }
    }

    fun setFavoriteCommand(action: String) =
        customLayoutMap.set(FAVORITE, customCommands.getValue(action))

    fun cancelCoroutineScope() = coroutineScope.cancel()

    private fun handleRepeatShuffleCommand(action: String, player: Player) {
        when (action) {
            REPEAT -> {
                player.repeatMode = Player.REPEAT_MODE_ONE
                setRepeatShuffleCommand(REPEAT_ONE)
            }

            REPEAT_ONE -> {
                player.repeatMode = Player.REPEAT_MODE_ALL
                player.shuffleModeEnabled = true
                setRepeatShuffleCommand(SHUFFLE)
            }

            SHUFFLE -> {
                player.shuffleModeEnabled = false
                player.repeatMode = Player.REPEAT_MODE_ALL
                setRepeatShuffleCommand(REPEAT)
            }
        }
    }

    private fun handleFavoriteCommand(action: String, player: Player) {
        val id = player.currentMediaItem?.mediaId ?: return
        when (action) {
            FAVORITE_ON -> {
                coroutineScope.launch { toggleFavoriteSongUseCase(id = id, isFavorite = false) }
            }

            FAVORITE_OFF -> {
                coroutineScope.launch { toggleFavoriteSongUseCase(id = id, isFavorite = true) }
            }
        }
    }

    private fun setRepeatShuffleCommand(action: String) =
        customLayoutMap.set(REPEAT_SHUFFLE, customCommands.getValue(action))

    private fun getAvailableCustomCommands() = mapOf(
        REPEAT to buildCustomCommand(
            action = REPEAT,
            displayName = context.getString(mediaCommonR.string.repeat),
            iconResource = MusicmaxIcons.Repeat.resourceId
        ),
        REPEAT_ONE to buildCustomCommand(
            action = REPEAT_ONE,
            displayName = context.getString(mediaCommonR.string.repeat_one),
            iconResource = MusicmaxIcons.RepeatOne.resourceId
        ),
        SHUFFLE to buildCustomCommand(
            action = SHUFFLE,
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
