/*
 * Copyright 2022 Afig Aliyev
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

package com.maximillianleonov.musicmax.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val DefaultSpace = 0.dp
private val ExtraSmallSpace = 4.dp
private val SmallSpace = 8.dp
private val SmallMediumSpace = 12.dp
private val MediumSpace = 16.dp
private val ExtraMediumSpace = 24.dp
private val LargeSpace = 32.dp
private val ExtraLargeSpace = 40.dp
private val LargestSpace = 64.dp

@Immutable
data class Spacing(
    val default: Dp = DefaultSpace,
    val extraSmall: Dp = ExtraSmallSpace,
    val small: Dp = SmallSpace,
    val smallMedium: Dp = SmallMediumSpace,
    val medium: Dp = MediumSpace,
    val extraMedium: Dp = ExtraMediumSpace,
    val large: Dp = LargeSpace,
    val extraLarge: Dp = ExtraLargeSpace,
    val largest: Dp = LargestSpace
)

internal val LocalSpacing = staticCompositionLocalOf { Spacing() }
