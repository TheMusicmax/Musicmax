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

import com.android.build.api.dsl.ManagedVirtualDevice
import com.android.build.api.variant.TestAndroidComponentsExtension
import com.android.build.gradle.TestExtension
import com.maximillianleonov.musicmax.configureKotlinAndroid
import com.maximillianleonov.musicmax.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create

class AndroidBenchmarkConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.plugins.android.test.get().pluginId)
            apply(libs.plugins.kotlin.android.get().pluginId)
        }

        extensions.configure<TestExtension> {
            configureKotlinAndroid(this)

            @Suppress("MagicNumber")
            defaultConfig {
                minSdk = 23
                targetSdk = libs.versions.android.targetSdk.get().toInt()

                testOptions.managedDevices.devices.create<ManagedVirtualDevice>("pixel2Api31") {
                    device = "Pixel 2"
                    apiLevel = 31
                    systemImageSource = "aosp"
                }
            }

            buildTypes {
                create("benchmark") {
                    signingConfig = signingConfigs.getByName("debug")
                    matchingFallbacks.add("release")
                    isDebuggable = true
                }
            }

            targetProjectPath = ":app"
            experimentalProperties["android.experimental.self-instrumenting"] = true
        }

        extensions.configure<TestAndroidComponentsExtension> {
            beforeVariants {
                it.enable = it.buildType == "benchmark"
            }
        }
    }
}
