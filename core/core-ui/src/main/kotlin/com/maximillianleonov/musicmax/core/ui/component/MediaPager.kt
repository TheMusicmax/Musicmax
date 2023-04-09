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

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.lerp
import com.maximillianleonov.musicmax.core.designsystem.component.MusicmaxTab
import com.maximillianleonov.musicmax.core.designsystem.component.MusicmaxTabRow
import com.maximillianleonov.musicmax.core.designsystem.theme.spacing
import com.maximillianleonov.musicmax.core.model.Album
import com.maximillianleonov.musicmax.core.model.Artist
import com.maximillianleonov.musicmax.core.model.Song
import com.maximillianleonov.musicmax.core.ui.R
import com.maximillianleonov.musicmax.core.ui.common.MediaTab
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaPager(
    songs: List<Song>,
    artists: List<Artist>,
    albums: List<Album>,
    onSongClick: (Int) -> Unit,
    onArtistClick: (Long) -> Unit,
    onAlbumClick: (Long) -> Unit,
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit,
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    val tabs = remember { MediaTab.values() }
    val pagerState = rememberPagerState()
    val selectedTabIndex = pagerState.currentPage

    Column(modifier = modifier) {
        MusicmaxTabRow(
            selectedTabIndex = selectedTabIndex,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            }
        ) {
            tabs.forEach { tab ->
                val index = tab.ordinal
                val selected = selectedTabIndex == index

                MusicmaxTab(
                    selected = selected,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(text = stringResource(id = tab.titleResource)) }
                )
            }
        }

        AnimatedVisibility(visible = songs.isNotEmpty()) {
            PlayOutlinedShuffleButtons(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small),
                onPlayClick = onPlayClick,
                onShuffleClick = onShuffleClick
            )
        }

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            pageCount = tabs.size,
            verticalAlignment = Alignment.Top
        ) { page ->
            when (page) {
                MediaTab.Songs.ordinal -> {
                    SongsTabContent(songs = songs, onClick = onSongClick)
                }
                MediaTab.Artists.ordinal -> {
                    ArtistsTabContent(artists = artists, onClick = onArtistClick)
                }
                MediaTab.Albums.ordinal -> {
                    AlbumsTabContent(albums = albums, onClick = onAlbumClick)
                }
            }
        }
    }
}

@Composable
private fun SongsTabContent(
    songs: List<Song>,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (songs.isNotEmpty()) {
        LazyColumn(modifier = modifier) {
            itemsIndexed(songs) { index, song ->
                SongItem(song = song, onClick = { onClick(index) })
            }
        }
    } else {
        EmptyContent(textResource = R.string.no_songs)
    }
}

@Composable
private fun ArtistsTabContent(
    artists: List<Artist>,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (artists.isNotEmpty()) {
        LazyColumn(modifier = modifier) {
            items(artists) { artist ->
                ArtistItem(artist = artist, onClick = { onClick(artist.id) })
            }
        }
    } else {
        EmptyContent(textResource = R.string.no_artists)
    }
}

@Composable
private fun AlbumsTabContent(
    albums: List<Album>,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (albums.isNotEmpty()) {
        LazyVerticalGrid(modifier = modifier, columns = GridCells.Fixed(count = ColumnsCount)) {
            items(albums) { album ->
                AlbumItem(album = album, onClick = { onClick(album.id) })
            }
        }
    } else {
        EmptyContent(textResource = R.string.no_albums)
    }
}

@Composable
private fun EmptyContent(@StringRes textResource: Int, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(id = textResource),
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun Modifier.pagerTabIndicatorOffset(
    pagerState: PagerState,
    tabPositions: List<TabPosition>,
    pageIndexMapping: (Int) -> Int = { it },
) = layout { measurable, constraints ->
    if (tabPositions.isEmpty()) {
        layout(constraints.maxWidth, 0) {}
    } else {
        val currentPage = minOf(tabPositions.lastIndex, pageIndexMapping(pagerState.currentPage))
        val currentTab = tabPositions[currentPage]
        val previousTab = tabPositions.getOrNull(currentPage - 1)
        val nextTab = tabPositions.getOrNull(currentPage + 1)
        val fraction = pagerState.currentPageOffsetFraction
        val indicatorWidth = if (fraction > 0 && nextTab != null) {
            lerp(currentTab.width, nextTab.width, fraction).roundToPx()
        } else if (fraction < 0 && previousTab != null) {
            lerp(currentTab.width, previousTab.width, -fraction).roundToPx()
        } else {
            currentTab.width.roundToPx()
        }
        val indicatorOffset = if (fraction > 0 && nextTab != null) {
            lerp(currentTab.left, nextTab.left, fraction).roundToPx()
        } else if (fraction < 0 && previousTab != null) {
            lerp(currentTab.left, previousTab.left, -fraction).roundToPx()
        } else {
            currentTab.left.roundToPx()
        }
        val placeable = measurable.measure(
            Constraints(
                minWidth = indicatorWidth,
                maxWidth = indicatorWidth,
                minHeight = 0,
                maxHeight = constraints.maxHeight
            )
        )
        layout(constraints.maxWidth, maxOf(placeable.height, constraints.minHeight)) {
            placeable.placeRelative(
                indicatorOffset,
                maxOf(constraints.minHeight - placeable.height, 0)
            )
        }
    }
}

private const val ColumnsCount = 2
