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

package com.maximillianleonov.musicmax.feature.album.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.maximillianleonov.musicmax.feature.album.AlbumRoute

private const val AlbumRoute = "album_route"

private const val AlbumIdArg = "album_id"
private const val AlbumRouteWithArguments = "$AlbumRoute/{$AlbumIdArg}"

internal fun SavedStateHandle.getAlbumId(): Long = checkNotNull(this[AlbumIdArg])

fun NavHostController.navigateToAlbum(albumId: Long) =
    navigate(route = "$AlbumRoute/$albumId") { launchSingleTop = true }

fun NavGraphBuilder.albumScreen(
    onNavigateToPlayer: () -> Unit,
    onBackClick: () -> Unit
) = composable(
    route = AlbumRouteWithArguments,
    arguments = listOf(
        navArgument(AlbumIdArg) { type = NavType.LongType }
    )
) {
    AlbumRoute(onNavigateToPlayer = onNavigateToPlayer, onBackClick = onBackClick)
}
