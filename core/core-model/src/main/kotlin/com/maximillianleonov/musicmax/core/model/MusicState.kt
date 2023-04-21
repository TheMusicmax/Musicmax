/*
 * Copyright 2023 Maximillian Leonov
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

package com.maximillianleonov.musicmax.core.model

import android.net.Uri
import com.maximillianleonov.musicmax.core.media.common.MediaConstants.DEFAULT_ALBUM_ID
import com.maximillianleonov.musicmax.core.media.common.MediaConstants.DEFAULT_ARTIST_ID
import com.maximillianleonov.musicmax.core.media.common.MediaConstants.DEFAULT_DURATION_MS
import com.maximillianleonov.musicmax.core.media.common.MediaConstants.DEFAULT_MEDIA_ID

data class MusicState(
    val currentSong: Song = Song(
        mediaId = DEFAULT_MEDIA_ID,
        artistId = DEFAULT_ARTIST_ID,
        albumId = DEFAULT_ALBUM_ID,
        mediaUri = Uri.EMPTY,
        artworkUri = Uri.EMPTY,
        title = "",
        artist = "",
        album = "",
        isFavorite = false
    ),
    val playbackState: PlaybackState = PlaybackState.IDLE,
    val playWhenReady: Boolean = false,
    val duration: Long = DEFAULT_DURATION_MS
)
