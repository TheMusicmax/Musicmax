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

package com.maximillianleonov.musicmax.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.maximillianleonov.musicmax.core.permission.rememberMusicmaxPermissionState
import com.maximillianleonov.musicmax.feature.favorite.navigation.FavoriteRoute
import com.maximillianleonov.musicmax.feature.home.navigation.HomeRoute
import com.maximillianleonov.musicmax.feature.library.model.LibraryType
import com.maximillianleonov.musicmax.feature.library.navigation.LibraryRouteWithArguments
import com.maximillianleonov.musicmax.feature.library.navigation.getLibraryType
import com.maximillianleonov.musicmax.feature.library.navigation.navigateToLibrary
import com.maximillianleonov.musicmax.feature.library.util.getTitleResource
import com.maximillianleonov.musicmax.feature.player.navigation.navigateToPlayer
import com.maximillianleonov.musicmax.feature.search.navigation.SearchRoute
import com.maximillianleonov.musicmax.feature.settings.navigation.SettingsRoute
import com.maximillianleonov.musicmax.navigation.TopLevelDestination
import com.maximillianleonov.musicmax.navigation.util.contains
import com.maximillianleonov.musicmax.feature.favorite.R as favoriteR
import com.maximillianleonov.musicmax.feature.home.R as homeR
import com.maximillianleonov.musicmax.feature.search.R as searchR
import com.maximillianleonov.musicmax.feature.settings.R as settingsR

@Composable
fun rememberMusicmaxAppState(
    navController: NavHostController = rememberNavController(),
    startDestination: TopLevelDestination = TopLevelDestination.Home
) = remember(navController, startDestination) {
    MusicmaxAppState(navController, startDestination)
}

@OptIn(ExperimentalPermissionsApi::class)
@Stable
class MusicmaxAppState(
    val navController: NavHostController,
    val startDestination: TopLevelDestination
) {
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    private val currentTopLevelDestination: TopLevelDestination
        @Composable get() {
            topLevelDestinations.firstOrNull { it.route in currentDestination }
                ?.let { _currentTopLevelDestination = it }
            return _currentTopLevelDestination
        }

    val isTopLevelDestination: Boolean
        @Composable get() = currentTopLevelDestination.route in currentDestination

    val shouldShowBackButton: Boolean
        @Composable get() = currentDestination != null && topLevelRoutes.none { it in currentDestination }

    val topLevelDestinations = TopLevelDestination.values()

    private var _currentTopLevelDestination by mutableStateOf(startDestination)
    private val topLevelRoutes = listOf(HomeRoute, SearchRoute, FavoriteRoute, SettingsRoute)

    val permissionState: PermissionState
        @Composable get() = rememberMusicmaxPermissionState { isPermissionRequested = true }

    var isPermissionRequested by mutableStateOf(false)
        private set

    fun navigateToTopLevelDestination(destination: TopLevelDestination) =
        navController.navigate(destination.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }

    fun navigateToPlayer() = navController.navigateToPlayer()

    fun navigateToArtist(prefix: String, artistId: Long) = navController.navigateToLibrary(
        prefix = prefix,
        libraryType = LibraryType.Artist,
        libraryId = artistId.toString()
    )

    fun navigateToAlbum(prefix: String, albumId: Long) = navController.navigateToLibrary(
        prefix = prefix,
        libraryType = LibraryType.Album,
        libraryId = albumId.toString()
    )

    fun navigateToFolder(prefix: String, name: String) = navController.navigateToLibrary(
        prefix = prefix,
        libraryType = LibraryType.Folder,
        libraryId = name
    )

    @Suppress("TopLevelComposableFunctions")
    @StringRes
    @Composable
    fun getTitleResource(): Int {
        val currentBackStackEntry = navController.currentBackStackEntryAsState().value
        return when (currentBackStackEntry?.destination?.route) {
            HomeRoute -> homeR.string.home
            SearchRoute -> searchR.string.search
            FavoriteRoute -> favoriteR.string.favorite
            SettingsRoute -> settingsR.string.settings
            LibraryRouteWithArguments -> currentBackStackEntry.getLibraryType().getTitleResource()
            else -> currentTopLevelDestination.titleResource
        }
    }

    fun onBackClick() = navController.popBackStack()
}
