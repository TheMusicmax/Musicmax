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

package com.maximillianleonov.musicmax.core.data.repository

import com.maximillianleonov.musicmax.core.data.mapper.asPlayingQueueEntity
import com.maximillianleonov.musicmax.core.data.mapper.asSongEntity
import com.maximillianleonov.musicmax.core.data.mapper.asSongModel
import com.maximillianleonov.musicmax.core.data.mapper.listMap
import com.maximillianleonov.musicmax.core.database.model.PlayingQueueEntity
import com.maximillianleonov.musicmax.core.database.model.SongEntity
import com.maximillianleonov.musicmax.core.database.source.SongDatabaseDataSource
import com.maximillianleonov.musicmax.core.domain.model.SongModel
import com.maximillianleonov.musicmax.core.domain.repository.SongRepository
import com.maximillianleonov.musicmax.core.mediastore.source.SongMediaStoreDataSource
import com.maximillianleonov.musicmax.core.model.Song
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
    private val songDatabaseDataSource: SongDatabaseDataSource,
    private val songMediaStoreDataSource: SongMediaStoreDataSource
) : SongRepository {
    override fun getAll(): Flow<List<SongModel>> =
        songDatabaseDataSource.getAll().listMap(SongEntity::asSongModel)

    override fun getPlayingQueue(): Flow<List<SongModel>> =
        songDatabaseDataSource.getPlayingQueue().listMap(PlayingQueueEntity::asSongModel)

    override suspend fun setPlayingQueue(songs: List<SongModel>) =
        songDatabaseDataSource.setPlayingQueue(entities = songs.map(SongModel::asPlayingQueueEntity))

    override suspend fun synchronize() {
        val songs = songMediaStoreDataSource.getAll().map(Song::asSongEntity)
        songDatabaseDataSource.deleteAndInsertAll(songs)
    }
}
