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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.unit.dp
import com.maximillianleonov.musicmax.core.designsystem.component.SingleLineText
import com.maximillianleonov.musicmax.core.designsystem.theme.spacing
import com.maximillianleonov.musicmax.core.model.SortBy
import com.maximillianleonov.musicmax.core.model.SortOrder
import com.maximillianleonov.musicmax.core.ui.component.OutlinedMediaHeader
import com.maximillianleonov.musicmax.core.ui.R as uiR

@Composable
internal fun ArtistHeader(
    name: String,
    numberOfSongs: Int,
    sortOrder: SortOrder,
    sortBy: SortBy,
    onChangeSortOrder: (SortOrder) -> Unit,
    onChangeSortBy: (SortBy) -> Unit,
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(HeaderElevation))
            .padding(MaterialTheme.spacing.small)
            .fillMaxWidth()
    ) {
        SingleLineText(
            text = name,
            shouldUseMarquee = true,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        SingleLineText(
            text = pluralStringResource(
                id = uiR.plurals.number_of_songs,
                count = numberOfSongs,
                numberOfSongs
            ),
            shouldUseMarquee = true,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
        OutlinedMediaHeader(
            sortOrder = sortOrder,
            sortBy = sortBy,
            onChangeSortOrder = onChangeSortOrder,
            onChangeSortBy = onChangeSortBy,
            onPlayClick = onPlayClick,
            onShuffleClick = onShuffleClick
        )
    }
}

private val HeaderElevation = 3.dp
