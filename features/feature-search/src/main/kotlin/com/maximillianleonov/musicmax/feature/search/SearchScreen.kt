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

package com.maximillianleonov.musicmax.feature.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maximillianleonov.musicmax.core.model.Album
import com.maximillianleonov.musicmax.core.model.Artist
import com.maximillianleonov.musicmax.core.model.Song
import com.maximillianleonov.musicmax.core.ui.component.MediaPager
import com.maximillianleonov.musicmax.feature.search.component.SearchTextField

@Composable
internal fun SearchRoute(
    onNavigateToPlayer: () -> Unit,
    onNavigateToArtist: (Long) -> Unit,
    onNavigateToAlbum: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val searchDetails by viewModel.searchDetails.collectAsStateWithLifecycle()

    SearchScreen(
        modifier = modifier,
        query = query,
        songs = searchDetails.songs,
        artists = searchDetails.artists,
        albums = searchDetails.albums,
        onQueryChange = viewModel::changeQuery,
        onSongClick = { startIndex ->
            viewModel.play(startIndex)
            onNavigateToPlayer()
        },
        onArtistClick = onNavigateToArtist,
        onAlbumClick = onNavigateToAlbum,
        onPlayClick = {
            viewModel.play()
            onNavigateToPlayer()
        },
        onShuffleClick = {
            viewModel.shuffle()
            onNavigateToPlayer()
        },
        onToggleFavorite = viewModel::onToggleFavorite
    )
}

@Composable
private fun SearchScreen(
    query: String,
    songs: List<Song>,
    artists: List<Artist>,
    albums: List<Album>,
    onQueryChange: (String) -> Unit,
    onSongClick: (Int) -> Unit,
    onArtistClick: (Long) -> Unit,
    onAlbumClick: (Long) -> Unit,
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit,
    onToggleFavorite: (id: String, isFavorite: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SearchTextField(query = query, onQueryChange = onQueryChange)
        AnimatedVisibility(
            visible = query.isNotBlank(),
            enter = MediaPagerEnterTransition,
            exit = MediaPagerExitTransition
        ) {
            MediaPager(
                songs = songs,
                artists = artists,
                albums = albums,
                onSongClick = onSongClick,
                onArtistClick = onArtistClick,
                onAlbumClick = onAlbumClick,
                onPlayClick = onPlayClick,
                onShuffleClick = onShuffleClick,
                onToggleFavorite = onToggleFavorite
            )
        }
    }
}

private val MediaPagerEnterTransition = fadeIn()
private val MediaPagerExitTransition = fadeOut()
