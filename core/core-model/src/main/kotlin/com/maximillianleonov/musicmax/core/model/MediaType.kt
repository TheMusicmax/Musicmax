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

package com.maximillianleonov.musicmax.core.model

import androidx.media3.common.MediaMetadata.FOLDER_TYPE_ALBUMS
import androidx.media3.common.MediaMetadata.FOLDER_TYPE_ARTISTS
import androidx.media3.common.MediaMetadata.FOLDER_TYPE_TITLES
import androidx.media3.common.MediaMetadata.FolderType

enum class MediaType(val mediaId: String, @FolderType val folderType: Int) {
    Song(mediaId = SongMediaId, folderType = FOLDER_TYPE_TITLES),
    Artist(mediaId = ArtistMediaId, folderType = FOLDER_TYPE_ARTISTS),
    Album(mediaId = AlbumMediaId, folderType = FOLDER_TYPE_ALBUMS);

    companion object {
        const val RootMediaId = "root_media_id"

        private val mediaTypes = values().associateBy(MediaType::mediaId)
        operator fun get(mediaId: String) = checkNotNull(mediaTypes[mediaId]) {
            "$InvalidMediaIdErrorMessage $mediaId"
        }
    }
}

private const val SongMediaId = "song_media_id"
private const val ArtistMediaId = "artist_media_id"
private const val AlbumMediaId = "album_media_id"

private const val InvalidMediaIdErrorMessage = "Invalid media id."
