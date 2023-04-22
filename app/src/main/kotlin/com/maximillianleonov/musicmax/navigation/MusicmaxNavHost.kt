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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.maximillianleonov.musicmax.feature.favorite.navigation.favoriteScreen
import com.maximillianleonov.musicmax.feature.home.navigation.HomeGraphRoute
import com.maximillianleonov.musicmax.feature.home.navigation.homeScreen
import com.maximillianleonov.musicmax.feature.library.navigation.libraryScreen
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
            onNavigateToArtist = { artistId -> onNavigateToArtist(HomeGraphRoute, artistId) },
            onNavigateToAlbum = { albumId -> onNavigateToAlbum(HomeGraphRoute, albumId) }
        ) {
            libraryScreen(
                prefix = HomeGraphRoute,
                onNavigateToPlayer = onNavigateToPlayer,
                onBackClick = onBackClick
            )
        }
        searchScreen(
            onNavigateToPlayer = onNavigateToPlayer,
            onNavigateToArtist = { artistId -> onNavigateToArtist(SearchGraphRoute, artistId) },
            onNavigateToAlbum = { albumId -> onNavigateToAlbum(SearchGraphRoute, albumId) }
        ) {
            libraryScreen(
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
