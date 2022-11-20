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

package com.maximillianleonov.musicmax.feature.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maximillianleonov.musicmax.core.model.Album
import com.maximillianleonov.musicmax.core.model.Artist
import com.maximillianleonov.musicmax.core.model.Song
import com.maximillianleonov.musicmax.core.ui.component.AlbumItem
import com.maximillianleonov.musicmax.core.ui.component.ArtistItem
import com.maximillianleonov.musicmax.core.ui.component.MediaPager
import com.maximillianleonov.musicmax.core.ui.component.SongItem

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun HomeRoute(
    onNavigateToPlayer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val songs by viewModel.songs.collectAsStateWithLifecycle()
    val artists by viewModel.artists.collectAsStateWithLifecycle()
    val albums by viewModel.albums.collectAsStateWithLifecycle()

    HomeScreen(
        modifier = modifier,
        songs = songs,
        artists = artists,
        albums = albums,
        onSongClick = { startIndex ->
            viewModel.play(startIndex)
            onNavigateToPlayer()
        }
    )
}

@Composable
private fun HomeScreen(
    songs: List<Song>,
    artists: List<Artist>,
    albums: List<Album>,
    onSongClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    MediaPager(
        modifier = modifier,
        songsTabContent = {
            LazyColumn {
                itemsIndexed(songs) { index, song ->
                    SongItem(song = song, onClick = { onSongClick(index) })
                }
            }
        },
        artistsTabContent = {
            LazyColumn {
                items(artists) { artist ->
                    ArtistItem(artist = artist, onClick = { /* TODO */ })
                }
            }
        },
        albumsTabContent = {
            LazyVerticalGrid(columns = GridCells.Fixed(count = 2)) {
                items(albums) { album ->
                    AlbumItem(album = album, onClick = { /* TODO */ })
                }
            }
        }
    )
}
