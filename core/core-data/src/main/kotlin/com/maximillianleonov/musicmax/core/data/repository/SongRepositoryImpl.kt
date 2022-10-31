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

import com.maximillianleonov.musicmax.core.common.dispatcher.Dispatcher
import com.maximillianleonov.musicmax.core.common.dispatcher.MusicmaxDispatchers.IO
import com.maximillianleonov.musicmax.core.data.mapper.asSongModel
import com.maximillianleonov.musicmax.core.domain.model.SongModel
import com.maximillianleonov.musicmax.core.domain.repository.SongRepository
import com.maximillianleonov.musicmax.core.mediastore.source.SongMediaStoreDataSource
import com.maximillianleonov.musicmax.core.model.Song
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
    private val songMediaStoreDataSource: SongMediaStoreDataSource,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : SongRepository {
    override suspend fun getAll(): List<SongModel> =
        withContext(ioDispatcher) { songMediaStoreDataSource.getAll().map(Song::asSongModel) }
}
