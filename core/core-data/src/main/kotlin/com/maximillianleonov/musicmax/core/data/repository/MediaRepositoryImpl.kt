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

import com.maximillianleonov.musicmax.core.datastore.PreferencesDataSource
import com.maximillianleonov.musicmax.core.domain.repository.MediaRepository
import com.maximillianleonov.musicmax.core.mediastore.source.MediaStoreDataSource
import com.maximillianleonov.musicmax.core.model.Album
import com.maximillianleonov.musicmax.core.model.Artist
import com.maximillianleonov.musicmax.core.model.Folder
import com.maximillianleonov.musicmax.core.model.Song
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    mediaStoreDataSource: MediaStoreDataSource,
    preferencesDataSource: PreferencesDataSource
) : MediaRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override val songs: Flow<List<Song>> =
        preferencesDataSource.userData
            .map { it.favoriteSongs }
            .flatMapLatest(mediaStoreDataSource::getSongs)

    override val artists: Flow<List<Artist>> = songs.map { songs ->
        songs.groupBy(Song::artistId).map { (artistId, songs) ->
            val song = songs.first()
            Artist(id = artistId, name = song.artist, songs = songs)
        }
    }

    override val albums: Flow<List<Album>> = songs.map { songs ->
        songs.groupBy(Song::albumId).map { (albumId, songs) ->
            val song = songs.first()
            Album(
                id = albumId,
                artworkUri = song.artworkUri,
                name = song.album,
                artist = song.artist,
                songs = songs
            )
        }
    }

    override val folders: Flow<List<Folder>> = songs.map { songs ->
        songs.groupBy(Song::folder).map { (name, songs) ->
            Folder(name = name, songs = songs)
        }
    }
}
