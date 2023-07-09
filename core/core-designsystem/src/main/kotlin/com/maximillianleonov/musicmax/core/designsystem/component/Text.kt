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

package com.maximillianleonov.musicmax.core.designsystem.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SingleLineText(
    text: String,
    modifier: Modifier = Modifier,
    shouldUseMarquee: Boolean = false,
    color: Color = Color.Unspecified,
    style: TextStyle = LocalTextStyle.current
) {
    Text(
        modifier = modifier.then(if (shouldUseMarquee) Modifier.basicMarquee() else Modifier),
        text = text,
        color = color,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = style
    )
}
