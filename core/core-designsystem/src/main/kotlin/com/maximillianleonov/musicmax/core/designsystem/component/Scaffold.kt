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

package com.maximillianleonov.musicmax.core.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicmaxScaffold(
    @StringRes titleResource: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (innerPadding: PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            MusicmaxTopAppBar(
                titleResource = titleResource,
                navigationIcon = { MusicmaxBackButton(onClick = onBackClick) }
            )
        },
        contentWindowInsets = ScaffoldWindowInsets,
        content = content
    )
}

private val ScaffoldWindowInsets = WindowInsets(left = 0, top = 0, right = 0, bottom = 0)
