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

package com.maximillianleonov.musicmax.feature.artist.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.maximillianleonov.musicmax.feature.artist.ArtistRoute

private const val ArtistRoute = "artist_route"

private const val ArtistIdArg = "artist_id"
private const val ArtistRouteWithArguments = "$ArtistRoute/{$ArtistIdArg}"

internal fun SavedStateHandle.getArtistId(): Long = checkNotNull(this[ArtistIdArg])

fun NavHostController.navigateToArtist(artistId: Long) =
    navigate(route = "$ArtistRoute/$artistId") { launchSingleTop = true }

fun NavGraphBuilder.artistScreen(
    onNavigateToPlayer: () -> Unit,
    onBackClick: () -> Unit
) = composable(
    route = ArtistRouteWithArguments,
    arguments = listOf(
        navArgument(ArtistIdArg) { type = NavType.LongType }
    )
) {
    ArtistRoute(onNavigateToPlayer = onNavigateToPlayer, onBackClick = onBackClick)
}
