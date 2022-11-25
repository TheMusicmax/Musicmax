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

package com.maximillianleonov.musicmax.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.maximillianleonov.musicmax.core.database.model.PlayingQueueEntity
import com.maximillianleonov.musicmax.core.database.util.Constants.Tables.PLAYING_QUEUE
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayingQueueDao {
    @Query("SELECT * FROM $PLAYING_QUEUE")
    fun getAll(): Flow<List<PlayingQueueEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<PlayingQueueEntity>)

    @Query("DELETE FROM $PLAYING_QUEUE")
    suspend fun deleteAll()

    @Transaction
    suspend fun deleteAndInsertAll(entities: List<PlayingQueueEntity>) {
        deleteAll()
        insertAll(entities)
    }
}
