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

package com.maximillianleonov.musicmax.feature.home.component

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.maximillianleonov.musicmax.core.model.Album
import com.maximillianleonov.musicmax.core.ui.component.AlbumItem

@Composable
internal fun AlbumsTabContent(
    albums: List<Album>,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(modifier = modifier, columns = GridCells.Fixed(count = ColumnsCount)) {
        itemsIndexed(albums) { index, album ->
            AlbumItem(album = album, onClick = { onClick(index) })
        }
    }
}

private const val ColumnsCount = 2
