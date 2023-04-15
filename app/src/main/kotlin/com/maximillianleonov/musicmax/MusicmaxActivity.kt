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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.google.android.gms.ads.MobileAds
import com.maximillianleonov.musicmax.core.ui.util.AdMobConfigProvider
import com.maximillianleonov.musicmax.core.ui.util.ProvideAdMobConfigProvider
import com.maximillianleonov.musicmax.ui.MusicmaxApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MusicmaxActivity : ComponentActivity() {
    @Inject lateinit var adMobConfigProvider: AdMobConfigProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ProvideAdMobConfigProvider(adMobConfigProvider = adMobConfigProvider) {
                MusicmaxApp()
            }
        }
    }
}
