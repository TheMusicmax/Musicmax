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

package com.maximillianleonov.musicmax.feature.library.util

import com.maximillianleonov.musicmax.feature.library.LibraryUiState
import com.maximillianleonov.musicmax.feature.library.R

internal fun LibraryUiState.getTitleResource() = when (this) {
    LibraryUiState.Loading -> R.string.loading
    is LibraryUiState.ArtistType -> R.string.artist
    is LibraryUiState.AlbumType -> R.string.album
    is LibraryUiState.FolderType -> R.string.folder
}

internal fun LibraryUiState.getSongs() = when (this) {
    LibraryUiState.Loading -> emptyList()
    is LibraryUiState.AlbumType -> album.songs
    is LibraryUiState.ArtistType -> artist.songs
    is LibraryUiState.FolderType -> folder.songs
}
