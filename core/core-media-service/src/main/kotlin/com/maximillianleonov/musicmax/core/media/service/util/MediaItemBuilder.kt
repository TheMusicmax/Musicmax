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

package com.maximillianleonov.musicmax.core.media.service.util

import android.net.Uri
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaItem.RequestMetadata
import androidx.media3.common.MediaMetadata
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

internal fun buildPlayableMediaItem(
    mediaId: String,
    artistId: Long,
    albumId: Long,
    mediaUri: Uri,
    artworkUri: Uri,
    title: String,
    artist: String,
    folder: String,
    duration: Long,
    date: LocalDateTime,
    isFavorite: Boolean
) = MediaItem.Builder()
    .setMediaId(mediaId)
    .setRequestMetadata(
        RequestMetadata.Builder()
            .setMediaUri(mediaUri)
            .build()
    )
    .setMediaMetadata(
        MediaMetadata.Builder()
            .setArtworkUri(artworkUri)
            .setTitle(title)
            .setArtist(artist)
            .setIsBrowsable(false)
            .setIsPlayable(true)
            .setExtras(
                bundleOf(
                    ARTIST_ID to artistId,
                    ALBUM_ID to albumId,
                    FOLDER to folder,
                    DURATION to duration,
                    DATE to date.toInstant(TimeZone.currentSystemDefault()).epochSeconds,
                    IS_FAVORITE_ID to isFavorite
                )
            )
            .build()
    )
    .build()

internal const val ARTIST_ID = "artist_id"
internal const val ALBUM_ID = "album_id"
internal const val FOLDER = "folder"
internal const val DURATION = "duration"
internal const val DATE = "date"
internal const val IS_FAVORITE_ID = "favorite_id"
