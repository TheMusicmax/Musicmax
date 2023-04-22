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

package com.maximillianleonov.musicmax.core.domain.repository

import com.maximillianleonov.musicmax.core.domain.model.AlbumModel
import com.maximillianleonov.musicmax.core.domain.model.ArtistModel
import com.maximillianleonov.musicmax.core.domain.model.FolderModel
import com.maximillianleonov.musicmax.core.domain.model.SongModel
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    val songs: Flow<List<SongModel>>
    val artists: Flow<List<ArtistModel>>
    val albums: Flow<List<AlbumModel>>
    val folders: Flow<List<FolderModel>>
}
