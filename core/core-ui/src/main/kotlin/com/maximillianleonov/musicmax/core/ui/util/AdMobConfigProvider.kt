/*
 * Copyright 2023 Maximillian Leonov
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

package com.maximillianleonov.musicmax.core.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

interface AdMobConfigProvider {
    val songsBannerUnitId: String
    val artistsBannerUnitId: String
}

@Composable
fun ProvideAdMobConfigProvider(
    adMobConfigProvider: AdMobConfigProvider,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalAdMobConfigProvider provides adMobConfigProvider,
        content = content
    )
}

val LocalAdMobConfigProvider = staticCompositionLocalOf<AdMobConfigProvider> {
    error("No AdMobController provided.")
}
