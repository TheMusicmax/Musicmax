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

plugins {
    id("musicmax.android.application")
    id("musicmax.android.application.compose")
    id("musicmax.android.hilt")
    id("musicmax.android.lint")
    id("musicmax.android.signing-config")
    id("musicmax.admob-config.provider")
    id("musicmax.firebase-config")
}

android {
    namespace = "com.maximillianleonov.musicmax"

    defaultConfig {
        applicationId = "com.maximillianleonov.musicmax"
        versionCode = 10
        versionName = "1.1.6"
    }

    buildFeatures.buildConfig = true
}

dependencies {
    implementation(project(":core:core-common"))
    implementation(project(":core:core-data"))
    implementation(project(":core:core-datastore"))
    implementation(project(":core:core-mediastore"))
    implementation(project(":core:core-domain"))
    implementation(project(":core:core-ui"))
    implementation(project(":core:core-designsystem"))
    implementation(project(":core:core-model"))
    implementation(project(":core:core-media-common"))
    implementation(project(":core:core-media-service"))
    implementation(project(":core:core-media-notification"))
    implementation(project(":core:core-permission"))

    implementation(project(":features:feature-home"))
    implementation(project(":features:feature-search"))
    implementation(project(":features:feature-favorite"))
    implementation(project(":features:feature-settings"))
    implementation(project(":features:feature-player"))
    implementation(project(":features:feature-artist"))
    implementation(project(":features:feature-album"))
    implementation(project(":features:feature-library"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.accompanist.systemuicontroller)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics.ktx)
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.play.services.ads)
}
