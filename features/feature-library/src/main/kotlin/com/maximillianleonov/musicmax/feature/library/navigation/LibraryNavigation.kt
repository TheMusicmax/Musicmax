/*
 * Copyright 2023 Afig Aliyev
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

package com.maximillianleonov.musicmax.feature.library.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.maximillianleonov.musicmax.feature.library.LibraryRoute
import com.maximillianleonov.musicmax.feature.library.model.LibraryType

private const val LibraryRoute = "library_route"

private const val LibraryTypeArg = "library_type"
private const val LibraryIdArg = "library_id"

const val LibraryRouteWithArguments = "$LibraryRoute/{$LibraryTypeArg}/{$LibraryIdArg}"

fun NavBackStackEntry.getLibraryType() =
    LibraryType[checkNotNull(arguments?.getString(LibraryTypeArg))]

internal fun SavedStateHandle.getLibraryType() = LibraryType[checkNotNull(this[LibraryTypeArg])]
internal fun SavedStateHandle.getLibraryId(): String = checkNotNull(this[LibraryIdArg])

fun NavController.navigateToLibrary(
    prefix: String,
    libraryType: LibraryType,
    libraryId: String
) = navigate(route = "${prefix}_$LibraryRoute/${libraryType.value}/$libraryId") {
    launchSingleTop = true
}

fun NavGraphBuilder.libraryScreen(
    prefix: String,
    onNavigateToPlayer: () -> Unit
) = composable(
    route = "${prefix}_$LibraryRouteWithArguments",
    arguments = listOf(
        navArgument(LibraryTypeArg) { type = NavType.StringType },
        navArgument(LibraryIdArg) { type = NavType.StringType }
    )
) {
    LibraryRoute(onNavigateToPlayer = onNavigateToPlayer)
}
