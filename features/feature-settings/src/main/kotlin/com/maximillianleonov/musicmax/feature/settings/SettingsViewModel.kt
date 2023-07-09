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

package com.maximillianleonov.musicmax.feature.settings

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maximillianleonov.musicmax.core.designsystem.theme.supportsDynamicTheming
import com.maximillianleonov.musicmax.core.domain.usecase.GetPrivacyPolicyUrlUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.GetRepoUrlUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.GetUserDataUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.GetVersionUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.SetDarkThemeConfigUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.SetDynamicColorUseCase
import com.maximillianleonov.musicmax.core.model.DarkThemeConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getUserDataUseCase: GetUserDataUseCase,
    getRepoUrlUseCase: GetRepoUrlUseCase,
    getPrivacyPolicyUrlUseCase: GetPrivacyPolicyUrlUseCase,
    getVersionUseCase: GetVersionUseCase,
    private val setDynamicColorUseCase: SetDynamicColorUseCase,
    private val setDarkThemeConfigUseCase: SetDarkThemeConfigUseCase
) : ViewModel() {
    val uiState = getUserDataUseCase()
        .map { userData ->
            SettingsUiState.Success(
                supportsDynamicTheming = supportsDynamicTheming(),
                useDynamicColor = userData.useDynamicColor,
                darkThemeConfig = userData.darkThemeConfig,
                repoUrl = getRepoUrlUseCase().toUri(),
                privacyPolicyUrl = getPrivacyPolicyUrlUseCase().toUri(),
                version = getVersionUseCase()
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = SettingsUiState.Loading
        )

    fun setDynamicColor(useDynamicColor: Boolean) =
        viewModelScope.launch { setDynamicColorUseCase(useDynamicColor) }

    fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) =
        viewModelScope.launch { setDarkThemeConfigUseCase(darkThemeConfig) }
}
