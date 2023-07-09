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

package com.maximillianleonov.musicmax.feature.settings.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.maximillianleonov.musicmax.core.designsystem.component.MusicmaxOutlinedBorder
import com.maximillianleonov.musicmax.core.designsystem.component.SingleLineText
import com.maximillianleonov.musicmax.core.designsystem.theme.spacing

internal fun LazyListScope.group(
    @StringRes titleResource: Int,
    content: @Composable ColumnScope.() -> Unit
) = item {
    Group(titleResource = titleResource, content = content)
}

@Composable
private fun Group(
    @StringRes titleResource: Int,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    OutlinedCard(
        modifier = modifier
            .padding(MaterialTheme.spacing.small)
            .fillMaxWidth(),
        border = MusicmaxOutlinedBorder
    ) {
        Column(
            modifier = Modifier.padding(vertical = MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            SingleLineText(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                text = stringResource(id = titleResource),
                shouldUseMarquee = true,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            content()
        }
    }
}
