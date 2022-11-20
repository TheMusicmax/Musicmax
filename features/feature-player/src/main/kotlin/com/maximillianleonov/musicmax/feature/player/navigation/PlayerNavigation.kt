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

package com.maximillianleonov.musicmax.feature.player.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.maximillianleonov.musicmax.feature.player.PlayerRoute

private const val PlayerRoute = "player_route"

fun NavHostController.navigateToPlayer() = navigate(route = PlayerRoute) { launchSingleTop = true }

fun NavGraphBuilder.playerScreen(
    onSetSystemBarsLightIcons: () -> Unit,
    onResetSystemBarsIcons: () -> Unit
) = composable(route = PlayerRoute) {
    PlayerRoute(
        onSetSystemBarsLightIcons = onSetSystemBarsLightIcons,
        onResetSystemBarsIcons = onResetSystemBarsIcons
    )
}
