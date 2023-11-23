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

package com.maximillianleonov.musicmax

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Configure Compose-specific options.
 */
internal fun Project.configureAndroidCompose(commonExtension: CommonExtension<*, *, *, *, *>) =
    with(commonExtension) {
        defaultConfig.vectorDrawables.useSupportLibrary = true
        buildFeatures.compose = true
        composeOptions.kotlinCompilerExtensionVersion =
            libs.versions.androidx.compose.compiler.get()

        dependencies {
            val bom = libs.androidx.compose.bom
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))
        }
    }
