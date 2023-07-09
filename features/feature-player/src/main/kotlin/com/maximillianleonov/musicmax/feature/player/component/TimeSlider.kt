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

package com.maximillianleonov.musicmax.feature.player.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.maximillianleonov.musicmax.core.designsystem.theme.spacing
import com.maximillianleonov.musicmax.feature.player.util.asFormattedString
import com.maximillianleonov.musicmax.feature.player.util.convertToProgress

@Composable
internal fun PlayerTimeSlider(
    currentPosition: Long,
    duration: Long,
    onSkipTo: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val progress by animateFloatAsState(
        targetValue = convertToProgress(count = currentPosition, total = duration),
        label = "ProgressAnimation"
    )

    Column(
        modifier = modifier
            .padding(horizontal = MaterialTheme.spacing.medium)
            .fillMaxWidth()
    ) {
        Slider(
            modifier = Modifier.fillMaxWidth(),
            value = progress,
            onValueChange = onSkipTo
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = currentPosition.asFormattedString(),
                style = MaterialTheme.typography.bodySmall,
                color = TimeTextColor
            )
            Text(
                text = duration.asFormattedString(),
                style = MaterialTheme.typography.bodySmall,
                color = TimeTextColor
            )
        }
    }
}

private val TimeTextColor = Color.Gray
