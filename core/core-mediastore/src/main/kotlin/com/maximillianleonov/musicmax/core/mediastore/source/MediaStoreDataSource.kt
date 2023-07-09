/*
 * Copyright 2023 Afig Aliyev
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

package com.maximillianleonov.musicmax.core.mediastore.source

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import com.maximillianleonov.musicmax.core.mediastore.util.MediaStoreConfig
import com.maximillianleonov.musicmax.core.mediastore.util.asArtworkUri
import com.maximillianleonov.musicmax.core.mediastore.util.asFolder
import com.maximillianleonov.musicmax.core.mediastore.util.asLocalDateTime
import com.maximillianleonov.musicmax.core.mediastore.util.buildMediaStoreSortOrder
import com.maximillianleonov.musicmax.core.mediastore.util.getLong
import com.maximillianleonov.musicmax.core.mediastore.util.getString
import com.maximillianleonov.musicmax.core.mediastore.util.observe
import com.maximillianleonov.musicmax.core.model.Song
import com.maximillianleonov.musicmax.core.model.SortBy
import com.maximillianleonov.musicmax.core.model.SortOrder
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MediaStoreDataSource @Inject constructor(private val contentResolver: ContentResolver) {
    fun getSongs(
        sortOrder: SortOrder,
        sortBy: SortBy,
        favoriteSongs: Set<String>,
        excludedFolders: List<String>
    ) = contentResolver.observe(uri = MediaStoreConfig.Song.Collection).map {
        buildList {
            contentResolver.query(
                MediaStoreConfig.Song.Collection,
                MediaStoreConfig.Song.Projection,
                buildString {
                    append("${MediaStore.Audio.Media.IS_MUSIC} != 0")
                    repeat(excludedFolders.size) {
                        append(" AND ${MediaStore.Audio.Media.DATA} NOT LIKE ?")
                    }
                },
                excludedFolders.map { "%$it%" }.toTypedArray(),
                buildMediaStoreSortOrder(sortOrder, sortBy)
            )?.use { cursor ->
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(MediaStore.Audio.Media._ID)
                    val artistId = cursor.getLong(MediaStore.Audio.Media.ARTIST_ID)
                    val albumId = cursor.getLong(MediaStore.Audio.Media.ALBUM_ID)
                    val title = cursor.getString(MediaStore.Audio.Media.TITLE)
                    val artist = cursor.getString(MediaStore.Audio.Media.ARTIST)
                    val album = cursor.getString(MediaStore.Audio.Media.ALBUM)
                    val duration = cursor.getLong(MediaStore.Audio.Media.DURATION)
                    val date = cursor.getLong(MediaStore.Audio.Media.DATE_ADDED)

                    val mediaId = id.toString()
                    val mediaUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    val folder = cursor.getString(MediaStore.Audio.Media.DATA).asFolder()

                    Song(
                        mediaId = mediaId,
                        artistId = artistId,
                        albumId = albumId,
                        mediaUri = mediaUri,
                        artworkUri = albumId.asArtworkUri(),
                        title = title,
                        artist = artist,
                        album = album,
                        folder = folder,
                        duration = duration,
                        date = date.asLocalDateTime(),
                        isFavorite = mediaId in favoriteSongs
                    ).let(::add)
                }
            }
        }
    }
}
