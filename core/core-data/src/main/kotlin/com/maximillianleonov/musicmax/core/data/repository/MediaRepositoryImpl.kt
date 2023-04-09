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

package com.maximillianleonov.musicmax.core.data.repository

import com.maximillianleonov.musicmax.core.data.mapper.asSongModel
import com.maximillianleonov.musicmax.core.data.mapper.listMap
import com.maximillianleonov.musicmax.core.domain.model.AlbumModel
import com.maximillianleonov.musicmax.core.domain.model.ArtistModel
import com.maximillianleonov.musicmax.core.domain.model.SongModel
import com.maximillianleonov.musicmax.core.domain.repository.MediaRepository
import com.maximillianleonov.musicmax.core.mediastore.source.MediaStoreDataSource
import com.maximillianleonov.musicmax.core.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    mediaStoreDataSource: MediaStoreDataSource
) : MediaRepository {
    override val songs: Flow<List<SongModel>> =
        mediaStoreDataSource.getSongs().listMap(Song::asSongModel)

    override val artists: Flow<List<ArtistModel>> = songs.map { songs ->
        songs.groupBy(SongModel::artistId).map { (artistId, songs) ->
            val song = songs.first()
            ArtistModel(
                id = artistId,
                name = song.artist,
                songs = songs
            )
        }
    }

    override val albums: Flow<List<AlbumModel>> = songs.map { songs ->
        songs.groupBy(SongModel::albumId).map { (albumId, songs) ->
            val song = songs.first()
            AlbumModel(
                id = albumId,
                artworkUri = song.artworkUri,
                name = song.album,
                artist = song.artist,
                songs = songs
            )
        }
    }
}
