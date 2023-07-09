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

package com.maximillianleonov.musicmax.feature.library.util

import androidx.annotation.StringRes
import com.maximillianleonov.musicmax.feature.library.LibraryUiState
import com.maximillianleonov.musicmax.feature.library.R
import com.maximillianleonov.musicmax.feature.library.model.LibraryType

@StringRes
fun LibraryType.getTitleResource() = when (this) {
    LibraryType.Artist -> R.string.artist
    LibraryType.Album -> R.string.album
    LibraryType.Folder -> R.string.folder
}

internal fun LibraryUiState.getSongs() = when (this) {
    LibraryUiState.Loading -> emptyList()
    is LibraryUiState.AlbumType -> album.songs
    is LibraryUiState.ArtistType -> artist.songs
    is LibraryUiState.FolderType -> folder.songs
}
