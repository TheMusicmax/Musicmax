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

package com.maximillianleonov.musicmax.feature.settings

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.maximillianleonov.musicmax.core.designsystem.icon.MusicmaxIcons
import com.maximillianleonov.musicmax.core.designsystem.theme.spacing
import com.maximillianleonov.musicmax.feature.settings.component.Group
import com.maximillianleonov.musicmax.feature.settings.component.InfoText
import com.maximillianleonov.musicmax.feature.settings.component.UrlText

@Composable
internal fun SettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    SettingsScreen(
        repoUrl = viewModel.repoUrl,
        privacyPolicyUrl = viewModel.privacyPolicyUrl,
        version = viewModel.version,
        modifier = modifier
    )
}

@Composable
fun SettingsScreen(
    repoUrl: Uri,
    privacyPolicyUrl: Uri,
    version: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.smallMedium)
    ) {
        Group(titleResource = R.string.about) {
            UrlText(
                icon = MusicmaxIcons.GitHub,
                textResource = R.string.source_code_github,
                url = repoUrl
            )
            UrlText(
                icon = MusicmaxIcons.Security,
                textResource = R.string.privacy_policy,
                url = privacyPolicyUrl
            )
            InfoText(
                icon = MusicmaxIcons.Info,
                textResource = R.string.version,
                info = version
            )
        }
    }
}
