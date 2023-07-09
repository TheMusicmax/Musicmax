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

package com.maximillianleonov.musicmax.feature.library

import com.maximillianleonov.musicmax.core.model.Album
import com.maximillianleonov.musicmax.core.model.Artist
import com.maximillianleonov.musicmax.core.model.Folder
import com.maximillianleonov.musicmax.core.model.SortBy
import com.maximillianleonov.musicmax.core.model.SortOrder

internal sealed interface LibraryUiState {
    object Loading : LibraryUiState

    data class ArtistType(
        val artist: Artist,
        val sortOrder: SortOrder,
        val sortBy: SortBy
    ) : LibraryUiState

    data class AlbumType(
        val album: Album,
        val sortOrder: SortOrder,
        val sortBy: SortBy
    ) : LibraryUiState

    data class FolderType(
        val folder: Folder,
        val sortOrder: SortOrder,
        val sortBy: SortBy
    ) : LibraryUiState
}
