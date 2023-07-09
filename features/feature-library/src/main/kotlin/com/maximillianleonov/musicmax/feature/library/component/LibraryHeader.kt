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

package com.maximillianleonov.musicmax.feature.library.component

import androidx.compose.foundation.lazy.LazyListScope
import com.maximillianleonov.musicmax.core.model.SortBy
import com.maximillianleonov.musicmax.core.model.SortOrder
import com.maximillianleonov.musicmax.feature.library.LibraryUiState

internal fun LazyListScope.libraryHeader(
    uiState: LibraryUiState,
    onChangeSortOrder: (SortOrder) -> Unit,
    onChangeSortBy: (SortBy) -> Unit,
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit,
) {
    item {
        when (uiState) {
            LibraryUiState.Loading -> Unit
            is LibraryUiState.ArtistType -> {
                val artist = uiState.artist
                ArtistHeader(
                    name = artist.name,
                    numberOfSongs = artist.songs.size,
                    sortOrder = uiState.sortOrder,
                    sortBy = uiState.sortBy,
                    onChangeSortOrder = onChangeSortOrder,
                    onChangeSortBy = onChangeSortBy,
                    onPlayClick = onPlayClick,
                    onShuffleClick = onShuffleClick
                )
            }

            is LibraryUiState.AlbumType -> {
                val album = uiState.album
                AlbumHeader(
                    name = album.name,
                    artist = album.artist,
                    artworkUri = album.artworkUri,
                    sortOrder = uiState.sortOrder,
                    sortBy = uiState.sortBy,
                    onChangeSortOrder = onChangeSortOrder,
                    onChangeSortBy = onChangeSortBy,
                    onPlayClick = onPlayClick,
                    onShuffleClick = onShuffleClick
                )
            }

            is LibraryUiState.FolderType -> {
                val folder = uiState.folder
                FolderHeader(
                    name = folder.name,
                    numberOfSongs = folder.songs.size,
                    sortOrder = uiState.sortOrder,
                    sortBy = uiState.sortBy,
                    onChangeSortOrder = onChangeSortOrder,
                    onChangeSortBy = onChangeSortBy,
                    onPlayClick = onPlayClick,
                    onShuffleClick = onShuffleClick
                )
            }
        }
    }
}
