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

package com.maximillianleonov.musicmax.core.ui.component

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.maximillianleonov.musicmax.core.designsystem.component.MusicmaxImage
import com.maximillianleonov.musicmax.core.designsystem.icon.MusicmaxIcons

@Composable
fun MusicmaxArtworkImage(
    artworkUri: Uri,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    colors: CardColors = CardDefaults.cardColors(containerColor = Color.Transparent),
    placeholder: @Composable () -> Unit = {
        Card(
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = MusicmaxIcons.Music.resourceId),
                contentDescription = contentDescription
            )
        }
    },
    contentScale: ContentScale = ContentScale.Crop
) {
    MusicmaxImage(
        modifier = modifier,
        model = artworkUri,
        contentDescription = contentDescription,
        shape = shape,
        colors = colors,
        loading = { placeholder() },
        error = { placeholder() },
        contentScale = contentScale
    )
}
