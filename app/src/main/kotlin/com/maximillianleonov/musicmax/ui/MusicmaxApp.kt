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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.maximillianleonov.musicmax.core.designsystem.theme.MusicmaxTheme
import com.maximillianleonov.musicmax.core.permission.PermissionContent
import com.maximillianleonov.musicmax.feature.player.mini.MiniPlayer
import com.maximillianleonov.musicmax.navigation.MusicmaxNavHost
import com.maximillianleonov.musicmax.ui.component.MusicmaxBottomBar

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MusicmaxApp(appState: MusicmaxAppState = rememberMusicmaxAppState()) {
    MusicmaxTheme {
        when (appState.permissionState.status) {
            PermissionStatus.Granted -> {
                MusicmaxAppContent(appState = appState)
            }
            is PermissionStatus.Denied -> {
                PermissionContent(
                    permissionState = appState.permissionState,
                    isPermissionRequested = appState.isPermissionRequested
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun MusicmaxAppContent(
    appState: MusicmaxAppState,
    modifier: Modifier = Modifier,
    topAppBarColors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(TopAppBarElevation)
    )
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AnimatedVisibility(
                visible = appState.isTopLevelDestination,
                enter = TopAppBarEnterTransition,
                exit = TopAppBarExitTransition
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(
                                id = appState.currentTopLevelDestination.titleResource
                            )
                        )
                    },
                    colors = topAppBarColors
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = appState.isTopLevelDestination,
                enter = BottomBarEnterTransition,
                exit = BottomBarExitTransition
            ) {
                MusicmaxBottomBar(
                    destinations = appState.topLevelDestinations,
                    currentDestination = appState.currentDestination,
                    onNavigateToDestination = appState::navigateToTopLevelDestination
                )
            }
        },
        contentWindowInsets = ScaffoldWindowInsets
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .consumedWindowInsets(innerPadding)
        ) {
            MusicmaxNavHost(
                modifier = Modifier.weight(1f),
                navController = appState.navController,
                startDestination = appState.startDestination.route,
                onNavigateToPlayer = appState::navigateToPlayerScreen
            )
            AnimatedVisibility(
                visible = appState.isTopLevelDestination,
                enter = MiniPlayerEnterTransition,
                exit = MiniPlayerExitTransition
            ) {
                MiniPlayer(onNavigateToPlayer = appState::navigateToPlayerScreen)
            }
        }
    }
}

private val ScaffoldWindowInsets = WindowInsets(left = 0, top = 0, right = 0, bottom = 0)

private val TopAppBarElevation = 3.dp
private val TopAppBarEnterTransition = fadeIn() + expandVertically(expandFrom = Alignment.Bottom)
private val TopAppBarExitTransition = shrinkVertically(shrinkTowards = Alignment.Bottom) + fadeOut()

private val BottomBarEnterTransition = fadeIn() + expandVertically(expandFrom = Alignment.Top)
private val BottomBarExitTransition = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()

private val MiniPlayerEnterTransition get() = BottomBarEnterTransition
private val MiniPlayerExitTransition get() = BottomBarExitTransition
