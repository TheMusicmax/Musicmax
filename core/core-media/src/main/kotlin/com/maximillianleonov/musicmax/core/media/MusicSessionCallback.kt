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

package com.maximillianleonov.musicmax.core.media

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata.FOLDER_TYPE_MIXED
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import androidx.media3.session.MediaSession
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.ListenableFuture
import com.maximillianleonov.musicmax.core.common.dispatcher.Dispatcher
import com.maximillianleonov.musicmax.core.common.dispatcher.MusicmaxDispatchers.MAIN
import com.maximillianleonov.musicmax.core.domain.model.SongModel
import com.maximillianleonov.musicmax.core.domain.usecase.GetSongsUseCase
import com.maximillianleonov.musicmax.core.media.mapper.asMediaItem
import com.maximillianleonov.musicmax.core.media.util.buildBrowsableMediaItem
import com.maximillianleonov.musicmax.core.model.MediaType
import com.maximillianleonov.musicmax.core.model.MediaType.Album
import com.maximillianleonov.musicmax.core.model.MediaType.Artist
import com.maximillianleonov.musicmax.core.model.MediaType.Companion.RootMediaId
import com.maximillianleonov.musicmax.core.model.MediaType.Song
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.guava.future
import javax.inject.Inject

class MusicSessionCallback @Inject constructor(
    @Dispatcher(MAIN) mainDispatcher: CoroutineDispatcher,
    private val getSongsUseCase: GetSongsUseCase
) : MediaLibrarySession.Callback {
    private val coroutineScope = CoroutineScope(mainDispatcher + SupervisorJob())

    override fun onGetLibraryRoot(
        session: MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<MediaItem>> = coroutineScope.future {
        LibraryResult.ofItem(
            buildBrowsableMediaItem(mediaId = RootMediaId, folderType = FOLDER_TYPE_MIXED),
            null
        )
    }

    override fun onGetChildren(
        session: MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> = coroutineScope.future {
        val mediaItems = when (parentId) {
            RootMediaId -> MediaType.values().map(MediaType::asMediaItem)
            Song.mediaId -> getSongsUseCase().first().map(SongModel::asMediaItem)
            Artist.mediaId -> TODO()
            Album.mediaId -> TODO()
            else -> error("$INVALID_MEDIA_ID_ERROR_MESSAGE $parentId")
        }
        LibraryResult.ofItemList(mediaItems, null)
    }

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: List<MediaItem>
    ): ListenableFuture<List<MediaItem>> = coroutineScope.future {
        mediaItems.map { mediaItem ->
            mediaItem.buildUpon()
                .setUri(mediaItem.requestMetadata.mediaUri)
                .build()
        }
    }

    fun cancelCoroutineScope() = coroutineScope.cancel()
}

private const val INVALID_MEDIA_ID_ERROR_MESSAGE = "Invalid media id."
