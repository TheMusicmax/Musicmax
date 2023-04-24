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

package com.maximillianleonov.musicmax

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.ads.MobileAds
import com.maximillianleonov.musicmax.MusicmaxUiState.Loading
import com.maximillianleonov.musicmax.core.designsystem.theme.MusicmaxTheme
import com.maximillianleonov.musicmax.core.model.DarkThemeConfig
import com.maximillianleonov.musicmax.core.ui.util.AdMobConfigProvider
import com.maximillianleonov.musicmax.core.ui.util.ProvideAdMobConfigProvider
import com.maximillianleonov.musicmax.ui.MusicmaxApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MusicmaxActivity : ComponentActivity() {
    @Inject lateinit var adMobConfigProvider: AdMobConfigProvider

    private val viewModel: MusicmaxViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)

        var uiState: MusicmaxUiState by mutableStateOf(Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { uiState = it }
            }
        }

        splashScreen.setKeepOnScreenCondition { uiState is Loading }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            val darkTheme = shouldUseDarkTheme(uiState = uiState)

            LaunchedEffect(systemUiController, darkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !darkTheme
            }

            ProvideAdMobConfigProvider(adMobConfigProvider = adMobConfigProvider) {
                MusicmaxTheme(
                    useDynamicColor = shouldUseDynamicColor(uiState = uiState),
                    darkTheme = darkTheme,
                ) {
                    MusicmaxApp()
                }
            }
        }
    }
}

@Composable
private fun shouldUseDynamicColor(uiState: MusicmaxUiState) = when (uiState) {
    Loading -> false
    is MusicmaxUiState.Success -> uiState.userData.useDynamicColor
}

@Composable
private fun shouldUseDarkTheme(uiState: MusicmaxUiState) = when (uiState) {
    Loading -> isSystemInDarkTheme()
    is MusicmaxUiState.Success -> when (uiState.userData.darkThemeConfig) {
        DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        DarkThemeConfig.LIGHT -> false
        DarkThemeConfig.DARK -> true
    }
}
