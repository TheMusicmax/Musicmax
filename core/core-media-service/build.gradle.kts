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

plugins {
    id("musicmax.android.library")
    id("musicmax.android.hilt")
}

android.namespace = "com.maximillianleonov.musicmax.core.media.service"

dependencies {
    api(project(":core:core-media-common"))
    implementation(project(":core:core-media-notification"))
    implementation(project(":core:core-designsystem"))
    implementation(project(":core:core-model"))
    implementation(project(":core:core-domain"))

    implementation(libs.bundles.androidx.media3)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.guava)
}
