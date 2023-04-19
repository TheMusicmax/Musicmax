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

package com.maximillianleonov.musicmax.core.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import com.maximillianleonov.musicmax.core.designsystem.component.MusicmaxCard
import com.maximillianleonov.musicmax.core.designsystem.component.SingleLineText
import com.maximillianleonov.musicmax.core.designsystem.theme.spacing
import com.maximillianleonov.musicmax.core.model.Artist
import com.maximillianleonov.musicmax.core.ui.R
import com.maximillianleonov.musicmax.core.ui.util.AdMobConfigProvider
import com.maximillianleonov.musicmax.core.ui.util.LocalAdMobConfigProvider

@Composable
internal fun Artists(
    artists: List<Artist>,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    adMobConfigProvider: AdMobConfigProvider = LocalAdMobConfigProvider.current
) {
    if (artists.isNotEmpty()) {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            item { BannerAd(adUnitId = adMobConfigProvider.artistsBannerUnitId) }

            items(artists) { artist ->
                ArtistItem(artist = artist, onClick = { onClick(artist.id) })
            }
        }
    } else {
        EmptyContent(textResource = R.string.no_artists)
    }
}

@Composable
private fun ArtistItem(
    artist: Artist,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
) {
    MusicmaxCard(modifier = modifier, onClick = onClick, colors = colors) {
        Column(
            modifier = Modifier
                .padding(MaterialTheme.spacing.small)
                .fillMaxWidth()
        ) {
            SingleLineText(
                text = artist.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            SingleLineText(
                text = pluralStringResource(
                    id = R.plurals.number_of_songs,
                    count = artist.songs.size,
                    artist.songs.size
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
