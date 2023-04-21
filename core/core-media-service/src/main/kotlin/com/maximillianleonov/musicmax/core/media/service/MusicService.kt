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

import androidx.media3.common.AudioAttributes
import androidx.media3.common.C.AUDIO_CONTENT_TYPE_MUSIC
import androidx.media3.common.C.USAGE_MEDIA
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.maximillianleonov.musicmax.core.common.dispatcher.Dispatcher
import com.maximillianleonov.musicmax.core.common.dispatcher.MusicmaxDispatchers.MAIN
import com.maximillianleonov.musicmax.core.domain.usecase.GetFavoriteSongIdsUseCase
import com.maximillianleonov.musicmax.core.media.notification.MusicNotificationProvider
import com.maximillianleonov.musicmax.core.media.service.util.unsafeLazy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MusicService : MediaLibraryService() {
    private var _mediaLibrarySession: MediaLibrarySession? = null
    private val mediaLibrarySession get() = checkNotNull(_mediaLibrarySession)

    @Inject lateinit var musicSessionCallback: MusicSessionCallback
    @Inject lateinit var musicNotificationProvider: MusicNotificationProvider
    @Inject lateinit var getFavoriteSongIdsUseCase: GetFavoriteSongIdsUseCase

    private val _currentMediaId = MutableStateFlow("")
    private val currentMediaId = _currentMediaId.asStateFlow()

    @Inject
    @Dispatcher(MAIN)
    lateinit var mainDispatcher: CoroutineDispatcher
    private val coroutineScope by unsafeLazy { CoroutineScope(mainDispatcher + SupervisorJob()) }

    override fun onCreate() {
        super.onCreate()

        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(USAGE_MEDIA)
            .build()

        val player = ExoPlayer.Builder(this)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true)
            .build()

        _mediaLibrarySession = MediaLibrarySession.Builder(this, player, musicSessionCallback)
            .build().apply { player.addListener(PlayerListener()) }

        setMediaNotificationProvider(musicNotificationProvider)
        startFavoriteSync()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaLibrarySession

    override fun onDestroy() {
        super.onDestroy()
        mediaLibrarySession.run {
            player.release()
            release()
            _mediaLibrarySession = null
        }
        musicSessionCallback.cancelCoroutineScope()
        musicNotificationProvider.cancelCoroutineScope()
    }

    private fun startFavoriteSync() = coroutineScope.launch {
        combine(currentMediaId, getFavoriteSongIdsUseCase()) { currentMediaId, favoriteSongIds ->
            currentMediaId in favoriteSongIds
        }.collectLatest { isCurrentMediaIdFavorite ->
            musicSessionCallback.toggleFavoriteAction(isFavorite = isCurrentMediaIdFavorite)
            mediaLibrarySession.setCustomLayout(musicSessionCallback.customLayout)
        }
    }

    private inner class PlayerListener : Player.Listener {
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            if (mediaItem == null) return
            _currentMediaId.update { mediaItem.mediaId }
        }
    }
}
