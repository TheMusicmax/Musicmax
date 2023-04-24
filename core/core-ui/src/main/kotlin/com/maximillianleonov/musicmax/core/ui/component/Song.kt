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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maximillianleonov.musicmax.core.designsystem.component.FavoriteButton
import com.maximillianleonov.musicmax.core.designsystem.component.MusicmaxCard
import com.maximillianleonov.musicmax.core.designsystem.component.SingleLineText
import com.maximillianleonov.musicmax.core.designsystem.theme.spacing
import com.maximillianleonov.musicmax.core.model.Song
import com.maximillianleonov.musicmax.core.ui.R
import com.maximillianleonov.musicmax.core.ui.util.AdMobConfigProvider
import com.maximillianleonov.musicmax.core.ui.util.LocalAdMobConfigProvider

@Composable
fun Songs(
    songs: List<Song>,
    currentPlayingSongId: String,
    onClick: (Int) -> Unit,
    onToggleFavorite: (id: String, isFavorite: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    adMobConfigProvider: AdMobConfigProvider = LocalAdMobConfigProvider.current
) {
    if (songs.isNotEmpty()) {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            item { BannerAd(adUnitId = adMobConfigProvider.songsBannerUnitId) }

            itemsIndexed(songs) { index, song ->
                SongItem(
                    song = song,
                    isPlaying = song.mediaId == currentPlayingSongId,
                    onClick = { onClick(index) },
                    onToggleFavorite = { isFavorite -> onToggleFavorite(song.mediaId, isFavorite) }
                )
            }
        }
    } else {
        EmptyContent(textResource = R.string.no_songs)
    }
}

fun LazyListScope.songs(
    songs: List<Song>,
    currentPlayingSongId: String,
    onClick: (Int) -> Unit,
    onToggleFavorite: (id: String, isFavorite: Boolean) -> Unit
) {
    if (songs.isNotEmpty()) {
        item { BannerAd(adUnitId = LocalAdMobConfigProvider.current.songsBannerUnitId) }

        itemsIndexed(songs) { index, song ->
            SongItem(
                song = song,
                isPlaying = song.mediaId == currentPlayingSongId,
                onClick = { onClick(index) },
                onToggleFavorite = { isFavorite -> onToggleFavorite(song.mediaId, isFavorite) }
            )
        }
    } else {
        item {
            EmptyContent(textResource = R.string.no_songs)
        }
    }
}

@Composable
private fun SongItem(
    song: Song,
    isPlaying: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: (isFavorite: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.cardColors(
        containerColor = if (isPlaying) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    ),
) {
    MusicmaxCard(modifier = modifier, onClick = onClick, shapeSize = 0.dp, colors = colors) {
        Row(
            modifier = Modifier
                .padding(MaterialTheme.spacing.small)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(SongDescriptionWeight),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.smallMedium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MusicmaxArtworkImage(
                    modifier = Modifier.size(SongCoverSize),
                    artworkUri = song.artworkUri,
                    contentDescription = song.title
                )

                Column {
                    SingleLineText(
                        text = song.title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isPlaying) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                    SingleLineText(
                        text = song.artist,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isPlaying) {
                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f)
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }

            val favoriteButtonColor = if (isPlaying) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.primary
            }
            FavoriteButton(
                modifier = Modifier.weight(FavoriteButtonWeight),
                isFavorite = song.isFavorite,
                onToggleFavorite = onToggleFavorite,
                colors = IconButtonDefaults.iconToggleButtonColors(
                    contentColor = favoriteButtonColor,
                    checkedContentColor = favoriteButtonColor
                )
            )
        }
    }
}

private const val SongDescriptionWeight = 0.9f
private const val FavoriteButtonWeight = 0.1f
private val SongCoverSize = 50.dp
