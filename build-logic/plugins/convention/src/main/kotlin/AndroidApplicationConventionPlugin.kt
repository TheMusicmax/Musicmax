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

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.maximillianleonov.musicmax.configureKotlinAndroid
import com.maximillianleonov.musicmax.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.plugins.android.application.get().pluginId)
            apply(libs.plugins.kotlin.android.get().pluginId)
        }

        extensions.configure<BaseAppModuleExtension> {
            configureKotlinAndroid(this)

            defaultConfig {
                targetSdk = libs.versions.android.targetSdk.get().toInt()

                buildTypes {
                    release {
                        isMinifyEnabled = true
                        isShrinkResources = true
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )

                        packagingOptions.resources.excludes += "DebugProbesKt.bin"
                    }

                    create("benchmark") {
                        initWith(getByName("release"))
                        signingConfig = signingConfigs.getByName("debug")
                        matchingFallbacks.add("release")
                        isDebuggable = false
                        proguardFiles("benchmark-rules.pro")
                    }
                }

                packagingOptions.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }
}
