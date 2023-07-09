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

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.maximillianleonov.musicmax.core.designsystem.component.MusicmaxButton
import com.maximillianleonov.musicmax.core.designsystem.component.MusicmaxOutlinedButton
import com.maximillianleonov.musicmax.core.designsystem.component.RadioButtonText
import com.maximillianleonov.musicmax.core.designsystem.component.SingleLineText
import com.maximillianleonov.musicmax.core.designsystem.icon.MusicmaxIcons
import com.maximillianleonov.musicmax.core.designsystem.theme.spacing
import com.maximillianleonov.musicmax.core.model.SortBy
import com.maximillianleonov.musicmax.core.model.SortOrder
import com.maximillianleonov.musicmax.core.ui.R
import com.maximillianleonov.musicmax.core.media.common.R as mediaCommonR

@Composable
fun MediaHeader(
    sortOrder: SortOrder,
    sortBy: SortBy,
    onChangeSortOrder: (SortOrder) -> Unit,
    onChangeSortBy: (SortBy) -> Unit,
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var shouldShowSortSection by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            PlayShuffleButton(
                modifier = Modifier.weight(1f),
                iconResource = MusicmaxIcons.Play.resourceId,
                textResource = mediaCommonR.string.play,
                onClick = onPlayClick
            )

            PlayShuffleButton(
                modifier = Modifier.weight(1f),
                iconResource = MusicmaxIcons.Shuffle.resourceId,
                textResource = mediaCommonR.string.shuffle,
                onClick = onShuffleClick
            )

            SortButton(
                isSortSectionShown = shouldShowSortSection,
                onClick = { shouldShowSortSection = !shouldShowSortSection },
                colors = IconButtonDefaults.iconButtonColors(contentColor = MediaHeaderContentColor)
            )
        }

        CompositionLocalProvider(LocalContentColor provides MediaHeaderContentColor) {
            AnimatedVisibility(visible = shouldShowSortSection) {
                SortSection(
                    sortOrder = sortOrder,
                    sortBy = sortBy,
                    onChangeSortOrder = onChangeSortOrder,
                    onChangeSortBy = onChangeSortBy,
                    radioButtonColors = RadioButtonDefaults.colors(
                        selectedColor = MediaHeaderContentColor,
                        unselectedColor = MediaHeaderContentColor.copy(alpha = 0.75f)
                    )
                )
            }
        }
    }
}

@Composable
fun OutlinedMediaHeader(
    sortOrder: SortOrder,
    sortBy: SortBy,
    onChangeSortOrder: (SortOrder) -> Unit,
    onChangeSortBy: (SortBy) -> Unit,
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var shouldShowSortSection by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            PlayShuffleButton(
                modifier = Modifier.weight(1f),
                iconResource = MusicmaxIcons.Play.resourceId,
                textResource = mediaCommonR.string.play,
                onClick = onPlayClick
            )

            OutlinedPlayShuffleButton(
                modifier = Modifier.weight(1f),
                iconResource = MusicmaxIcons.Shuffle.resourceId,
                textResource = mediaCommonR.string.shuffle,
                onClick = onShuffleClick
            )

            SortButton(
                isSortSectionShown = shouldShowSortSection,
                onClick = { shouldShowSortSection = !shouldShowSortSection }
            )
        }

        AnimatedVisibility(visible = shouldShowSortSection) {
            SortSection(
                sortOrder = sortOrder,
                sortBy = sortBy,
                onChangeSortOrder = onChangeSortOrder,
                onChangeSortBy = onChangeSortBy
            )
        }
    }
}

@Composable
private fun PlayShuffleButton(
    @DrawableRes iconResource: Int,
    @StringRes textResource: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MusicmaxButton(modifier = modifier, onClick = onClick) {
        Icon(
            modifier = Modifier.size(IconSize),
            painter = painterResource(id = iconResource),
            contentDescription = stringResource(id = textResource)
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
        SingleLineText(text = stringResource(id = textResource), shouldUseMarquee = true)
    }
}

@Composable
private fun OutlinedPlayShuffleButton(
    @DrawableRes iconResource: Int,
    @StringRes textResource: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MusicmaxOutlinedButton(modifier = modifier, onClick = onClick) {
        Icon(
            modifier = Modifier.size(IconSize),
            painter = painterResource(id = iconResource),
            contentDescription = stringResource(id = textResource)
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
        SingleLineText(text = stringResource(id = textResource), shouldUseMarquee = true)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SortButton(
    isSortSectionShown: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
) {
    val rotateValue by animateFloatAsState(
        targetValue = if (isSortSectionShown) SortRotateValue else 0f,
        label = "RotateAnimation"
    )

    IconButton(modifier = modifier, onClick = onClick, colors = colors) {
        AnimatedContent(
            modifier = Modifier.rotate(rotateValue),
            targetState = isSortSectionShown,
            transitionSpec = { scaleIn() with scaleOut() },
            label = "SortIconAnimation"
        ) { targetIsSortSectionShown ->
            if (targetIsSortSectionShown) {
                Icon(
                    imageVector = MusicmaxIcons.Close.imageVector,
                    contentDescription = stringResource(id = R.string.close)
                )
            } else {
                Icon(
                    painter = painterResource(id = MusicmaxIcons.Sort.resourceId),
                    contentDescription = stringResource(id = R.string.sort)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SortSection(
    sortOrder: SortOrder,
    sortBy: SortBy,
    onChangeSortOrder: (SortOrder) -> Unit,
    onChangeSortBy: (SortBy) -> Unit,
    modifier: Modifier = Modifier,
    radioButtonColors: RadioButtonColors = RadioButtonDefaults.colors()
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small),
            text = stringResource(id = R.string.sort_order),
            style = MaterialTheme.typography.titleMedium
        )

        Row {
            RadioButtonText(
                modifier = Modifier.weight(1f),
                textRes = R.string.ascending,
                isSelected = sortOrder == SortOrder.ASCENDING,
                onClick = { onChangeSortOrder(SortOrder.ASCENDING) },
                colors = radioButtonColors
            )
            RadioButtonText(
                modifier = Modifier.weight(1f),
                textRes = R.string.descending,
                isSelected = sortOrder == SortOrder.DESCENDING,
                onClick = { onChangeSortOrder(SortOrder.DESCENDING) },
                colors = radioButtonColors
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

        Text(
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small),
            text = stringResource(id = R.string.sort_by),
            style = MaterialTheme.typography.titleMedium
        )

        FlowRow(maxItemsInEachRow = 2) {
            SortBy.values().forEach { sortByItem ->
                RadioButtonText(
                    modifier = Modifier.weight(1f),
                    textRes = sortByStringResourcesMap.getValue(sortByItem),
                    isSelected = sortBy == sortByItem,
                    onClick = { onChangeSortBy(sortByItem) },
                    colors = radioButtonColors
                )
            }
        }
    }
}

private val sortByStringResourcesMap = mapOf(
    SortBy.TITLE to R.string.title,
    SortBy.ARTIST to R.string.artist,
    SortBy.ALBUM to R.string.album,
    SortBy.DURATION to R.string.duration,
    SortBy.DATE to R.string.date
)

private val IconSize = 20.dp
private val MediaHeaderContentColor = Color.White

private const val SortRotateValue = 180f
