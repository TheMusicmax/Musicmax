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

import com.maximillianleonov.musicmax.core.data.mapper.asArtistEntity
import com.maximillianleonov.musicmax.core.data.mapper.asArtistModel
import com.maximillianleonov.musicmax.core.data.mapper.listMap
import com.maximillianleonov.musicmax.core.database.model.ArtistEntity
import com.maximillianleonov.musicmax.core.database.source.ArtistDatabaseDataSource
import com.maximillianleonov.musicmax.core.domain.model.ArtistModel
import com.maximillianleonov.musicmax.core.domain.repository.ArtistRepository
import com.maximillianleonov.musicmax.core.mediastore.source.ArtistMediaStoreDataSource
import com.maximillianleonov.musicmax.core.model.Artist
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
    private val artistDatabaseDataSource: ArtistDatabaseDataSource,
    private val artistMediaStoreDataSource: ArtistMediaStoreDataSource
) : ArtistRepository {
    override fun getAll(): Flow<List<ArtistModel>> =
        artistDatabaseDataSource.getAll().listMap(ArtistEntity::asArtistModel)

    override suspend fun synchronize() {
        val artists = artistMediaStoreDataSource.getAll().map(Artist::asArtistEntity)
        artistDatabaseDataSource.deleteAndInsertAll(artists)
    }
}
