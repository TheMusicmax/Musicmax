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

package com.maximillianleonov.musicmax.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.maximillianleonov.musicmax.feature.album.navigation.albumScreen
import com.maximillianleonov.musicmax.feature.artist.navigation.artistScreen
import com.maximillianleonov.musicmax.feature.favorite.navigation.favoriteScreen
import com.maximillianleonov.musicmax.feature.home.navigation.HomeGraphRoute
import com.maximillianleonov.musicmax.feature.home.navigation.homeScreen
import com.maximillianleonov.musicmax.feature.player.navigation.playerScreen
import com.maximillianleonov.musicmax.feature.search.navigation.SearchGraphRoute
import com.maximillianleonov.musicmax.feature.search.navigation.searchScreen
import com.maximillianleonov.musicmax.feature.settings.navigation.settingsScreen

@Composable
fun MusicmaxNavHost(
    navController: NavHostController,
    startDestination: String,
    onNavigateToPlayer: () -> Unit,
    onNavigateToArtist: (prefix: String, artistId: Long) -> Unit,
    onNavigateToAlbum: (prefix: String, albumId: Long) -> Unit,
    onSetSystemBarsLightIcons: () -> Unit,
    onResetSystemBarsIcons: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        homeScreen(
            onNavigateToPlayer = onNavigateToPlayer,
            onNavigateToArtist = { onNavigateToArtist(HomeGraphRoute, it) },
            onNavigateToAlbum = { onNavigateToAlbum(HomeGraphRoute, it) }
        ) {
            nestedGraphs(
                prefix = HomeGraphRoute,
                onNavigateToPlayer = onNavigateToPlayer,
                onBackClick = onBackClick
            )
        }
        searchScreen(
            onNavigateToPlayer = onNavigateToPlayer,
            onNavigateToArtist = { onNavigateToArtist(SearchGraphRoute, it) },
            onNavigateToAlbum = { onNavigateToAlbum(SearchGraphRoute, it) }
        ) {
            nestedGraphs(
                prefix = SearchGraphRoute,
                onNavigateToPlayer = onNavigateToPlayer,
                onBackClick = onBackClick
            )
        }
        favoriteScreen(onNavigateToPlayer = onNavigateToPlayer)
        settingsScreen()
        playerScreen(
            onSetSystemBarsLightIcons = onSetSystemBarsLightIcons,
            onResetSystemBarsIcons = onResetSystemBarsIcons
        )
    }
}

private fun NavGraphBuilder.nestedGraphs(
    prefix: String,
    onNavigateToPlayer: () -> Unit,
    onBackClick: () -> Unit
) {
    artistScreen(
        prefix = prefix,
        onNavigateToPlayer = onNavigateToPlayer,
        onBackClick = onBackClick
    )
    albumScreen(
        prefix = prefix,
        onNavigateToPlayer = onNavigateToPlayer,
        onBackClick = onBackClick
    )
}
