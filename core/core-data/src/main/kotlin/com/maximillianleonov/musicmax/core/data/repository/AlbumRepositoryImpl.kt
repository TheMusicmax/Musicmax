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

import com.maximillianleonov.musicmax.core.data.mapper.asAlbumEntity
import com.maximillianleonov.musicmax.core.data.mapper.asAlbumModel
import com.maximillianleonov.musicmax.core.data.mapper.listMap
import com.maximillianleonov.musicmax.core.database.model.AlbumEntity
import com.maximillianleonov.musicmax.core.database.source.AlbumDatabaseDataSource
import com.maximillianleonov.musicmax.core.domain.model.AlbumModel
import com.maximillianleonov.musicmax.core.domain.repository.AlbumRepository
import com.maximillianleonov.musicmax.core.mediastore.source.AlbumMediaStoreDataSource
import com.maximillianleonov.musicmax.core.model.Album
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val albumDatabaseDataSource: AlbumDatabaseDataSource,
    private val albumMediaStoreDataSource: AlbumMediaStoreDataSource
) : AlbumRepository {
    override fun getAll(): Flow<List<AlbumModel>> =
        albumDatabaseDataSource.getAll().listMap(AlbumEntity::asAlbumModel)

    override suspend fun synchronize() {
        val albums = albumMediaStoreDataSource.getAll().map(Album::asAlbumEntity)
        albumDatabaseDataSource.deleteAndInsertAll(albums)
    }
}
