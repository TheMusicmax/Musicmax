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

package com.maximillianleonov.musicmax.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.maximillianleonov.musicmax.feature.home.HomeRoute

const val HomeGraphRoute = "home_graph"
const val HomeRoute = "home_route"

fun NavGraphBuilder.homeScreen(
    onNavigateToPlayer: () -> Unit,
    onNavigateToArtist: (Long) -> Unit,
    onNavigateToAlbum: (Long) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit
) = navigation(route = HomeGraphRoute, startDestination = HomeRoute) {
    composable(route = HomeRoute) {
        HomeRoute(
            onNavigateToPlayer = onNavigateToPlayer,
            onNavigateToArtist = onNavigateToArtist,
            onNavigateToAlbum = onNavigateToAlbum
        )
    }
    nestedGraphs()
}
