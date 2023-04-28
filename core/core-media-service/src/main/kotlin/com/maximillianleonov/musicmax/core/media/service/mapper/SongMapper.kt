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

package com.maximillianleonov.musicmax.core.media.service.mapper

import android.net.Uri
import androidx.media3.common.MediaItem
import com.maximillianleonov.musicmax.core.media.common.MediaConstants.DEFAULT_ALBUM_ID
import com.maximillianleonov.musicmax.core.media.common.MediaConstants.DEFAULT_ARTIST_ID
import com.maximillianleonov.musicmax.core.media.common.MediaConstants.DEFAULT_DURATION_MS
import com.maximillianleonov.musicmax.core.media.common.MediaConstants.DEFAULT_MEDIA_ID
import com.maximillianleonov.musicmax.core.media.service.util.ALBUM_ID
import com.maximillianleonov.musicmax.core.media.service.util.ARTIST_ID
import com.maximillianleonov.musicmax.core.media.service.util.DATE
import com.maximillianleonov.musicmax.core.media.service.util.DURATION
import com.maximillianleonov.musicmax.core.media.service.util.FOLDER
import com.maximillianleonov.musicmax.core.media.service.util.IS_FAVORITE_ID
import com.maximillianleonov.musicmax.core.media.service.util.buildPlayableMediaItem
import com.maximillianleonov.musicmax.core.model.Song
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal fun Song.asMediaItem() = buildPlayableMediaItem(
    mediaId = mediaId,
    artistId = artistId,
    albumId = albumId,
    mediaUri = mediaUri,
    artworkUri = artworkUri,
    title = title,
    artist = artist,
    folder = folder,
    duration = duration,
    date = date,
    isFavorite = isFavorite
)

internal fun MediaItem?.asSong() = Song(
    mediaId = this?.mediaId ?: DEFAULT_MEDIA_ID,
    artistId = this?.mediaMetadata?.extras?.getLong(ARTIST_ID) ?: DEFAULT_ARTIST_ID,
    albumId = this?.mediaMetadata?.extras?.getLong(ALBUM_ID) ?: DEFAULT_ALBUM_ID,
    mediaUri = this?.requestMetadata?.mediaUri.orEmpty(),
    artworkUri = this?.mediaMetadata?.artworkUri.orEmpty(),
    title = this?.mediaMetadata?.title.orEmpty(),
    artist = this?.mediaMetadata?.artist.orEmpty(),
    album = this?.mediaMetadata?.albumTitle.orEmpty(),
    folder = this?.mediaMetadata?.extras?.getString(FOLDER).orEmpty(),
    duration = this?.mediaMetadata?.extras?.getLong(DURATION) ?: DEFAULT_DURATION_MS,
    date = this?.mediaMetadata?.extras?.getLong(DATE).orEmpty().asLocalDateTime(),
    isFavorite = this?.mediaMetadata?.extras?.getBoolean(IS_FAVORITE_ID) == true
)

private fun Uri?.orEmpty() = this ?: Uri.EMPTY
private fun CharSequence?.orEmpty() = (this ?: "").toString()
private fun Long?.orEmpty() = this ?: 0L

private fun Long.asLocalDateTime() =
    Instant.fromEpochSeconds(this).toLocalDateTime(TimeZone.currentSystemDefault())
