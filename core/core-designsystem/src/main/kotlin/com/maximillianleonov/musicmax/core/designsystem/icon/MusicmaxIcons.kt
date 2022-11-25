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

package com.maximillianleonov.musicmax.core.designsystem.icon

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.maximillianleonov.musicmax.core.designsystem.R
import com.maximillianleonov.musicmax.core.designsystem.icon.Icon.DrawableResourceIcon
import com.maximillianleonov.musicmax.core.designsystem.icon.Icon.ImageVectorIcon

object MusicmaxIcons {
    val Home = ImageVectorIcon(Icons.Rounded.Home)
    val Search = ImageVectorIcon(Icons.Rounded.Search)
    val Favorite = ImageVectorIcon(Icons.Rounded.Favorite)
    val Settings = ImageVectorIcon(Icons.Rounded.Settings)
    val ArrowBack = ImageVectorIcon(Icons.Rounded.ArrowBack)
    val Clear = ImageVectorIcon(Icons.Rounded.Clear)
    val Music = DrawableResourceIcon(R.drawable.ic_music)
    val Repeat = DrawableResourceIcon(R.drawable.ic_repeat)
    val RepeatOne = DrawableResourceIcon(R.drawable.ic_repeat_one)
    val Shuffle = DrawableResourceIcon(R.drawable.ic_shuffle)
    val SkipPrevious = DrawableResourceIcon(R.drawable.ic_skip_previous)
    val Play = DrawableResourceIcon(R.drawable.ic_play)
    val Pause = DrawableResourceIcon(R.drawable.ic_pause)
    val SkipNext = DrawableResourceIcon(R.drawable.ic_skip_next)
    val GitHub = DrawableResourceIcon(R.drawable.ic_github)
    val Info = ImageVectorIcon(Icons.Rounded.Info)
    val Security = DrawableResourceIcon(R.drawable.ic_security)
}

sealed interface Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon
    data class DrawableResourceIcon(@DrawableRes val resourceId: Int) : Icon
}
