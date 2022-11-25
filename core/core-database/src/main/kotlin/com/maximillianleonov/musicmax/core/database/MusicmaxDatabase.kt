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

package com.maximillianleonov.musicmax.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.maximillianleonov.musicmax.core.database.dao.AlbumDao
import com.maximillianleonov.musicmax.core.database.dao.ArtistDao
import com.maximillianleonov.musicmax.core.database.dao.PlayingQueueDao
import com.maximillianleonov.musicmax.core.database.dao.SongDao
import com.maximillianleonov.musicmax.core.database.model.AlbumEntity
import com.maximillianleonov.musicmax.core.database.model.ArtistEntity
import com.maximillianleonov.musicmax.core.database.model.PlayingQueueEntity
import com.maximillianleonov.musicmax.core.database.model.SongEntity

@Database(
    entities = [SongEntity::class, ArtistEntity::class, AlbumEntity::class, PlayingQueueEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MusicmaxDatabase : RoomDatabase() {
    abstract val songDao: SongDao
    abstract val artistDao: ArtistDao
    abstract val albumDao: AlbumDao
    abstract val playingQueueDao: PlayingQueueDao
}
