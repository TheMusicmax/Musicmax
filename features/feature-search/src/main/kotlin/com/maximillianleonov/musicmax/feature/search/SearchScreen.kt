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
import com.maximillianleonov.musicmax.core.model.Folder
import com.maximillianleonov.musicmax.core.model.MusicState
import com.maximillianleonov.musicmax.core.model.Song
import com.maximillianleonov.musicmax.core.ui.component.MediaPager
import com.maximillianleonov.musicmax.feature.search.component.SearchTextField

@Composable
internal fun SearchRoute(
    onNavigateToPlayer: () -> Unit,
    onNavigateToArtist: (Long) -> Unit,
    onNavigateToAlbum: (Long) -> Unit,
    onNavigateToFolder: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val musicState by viewModel.musicState.collectAsStateWithLifecycle()
    val searchDetails by viewModel.searchDetails.collectAsStateWithLifecycle()

    SearchScreen(
        modifier = modifier,
        query = query,
        musicState = musicState,
        songs = searchDetails.songs,
        artists = searchDetails.artists,
        albums = searchDetails.albums,
        folders = searchDetails.folders,
        onQueryChange = viewModel::changeQuery,
        onSongClick = { startIndex ->
            viewModel.play(startIndex)
            onNavigateToPlayer()
        },
        onArtistClick = onNavigateToArtist,
        onAlbumClick = onNavigateToAlbum,
        onFolderClick = onNavigateToFolder,
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
    musicState: MusicState,
    songs: List<Song>,
    artists: List<Artist>,
    albums: List<Album>,
    folders: List<Folder>,
    onQueryChange: (String) -> Unit,
    onSongClick: (Int) -> Unit,
    onArtistClick: (Long) -> Unit,
    onAlbumClick: (Long) -> Unit,
    onFolderClick: (String) -> Unit,
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
                currentPlayingSongId = musicState.currentSong.mediaId,
                artists = artists,
                albums = albums,
                folders = folders,
                onSongClick = onSongClick,
                onArtistClick = onArtistClick,
                onAlbumClick = onAlbumClick,
                onFolderClick = onFolderClick,
                onPlayClick = onPlayClick,
                onShuffleClick = onShuffleClick,
                onToggleFavorite = onToggleFavorite
            )
        }
    }
}

private val MediaPagerEnterTransition = fadeIn()
private val MediaPagerExitTransition = fadeOut()
