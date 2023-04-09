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

package com.maximillianleonov.musicmax.feature.settings.component

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.maximillianleonov.musicmax.core.designsystem.component.SingleLineText
import com.maximillianleonov.musicmax.core.designsystem.icon.Icon
import com.maximillianleonov.musicmax.core.designsystem.theme.spacing

@Composable
internal fun UrlText(
    icon: Icon,
    @StringRes textResource: Int,
    url: Uri,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current
) {
    Row(
        modifier = modifier
            .clickable { context.openUrl(url) }
            .padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.small
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Icon(icon = icon, contentDescriptionResource = textResource)

        Column {
            Text(
                text = stringResource(id = textResource),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            SingleLineText(
                text = url.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
internal fun InfoText(
    icon: Icon,
    @StringRes textResource: Int,
    info: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.small
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Icon(icon = icon, contentDescriptionResource = textResource)

        Column {
            Text(
                text = stringResource(id = textResource),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = info,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun Icon(
    icon: Icon,
    @StringRes contentDescriptionResource: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
            .size(IconBoxSize),
        contentAlignment = Alignment.Center
    ) {
        when (icon) {
            is Icon.ImageVectorIcon -> {
                Icon(
                    imageVector = icon.imageVector,
                    contentDescription = stringResource(id = contentDescriptionResource),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            is Icon.DrawableResourceIcon -> {
                Icon(
                    painter = painterResource(id = icon.resourceId),
                    contentDescription = stringResource(id = contentDescriptionResource),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

private fun Context.openUrl(url: Uri) = startActivity(Intent(Intent.ACTION_VIEW, url))

private val IconBoxSize = 32.dp
