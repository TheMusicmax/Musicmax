/*
 * Copyright 2023 Afig Aliyev
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
import com.maximillianleonov.musicmax.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class FirebaseConfigConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val googleServicesFile = rootProject.file("app/google-services.json")
        if (googleServicesFile.exists()) {
            with(pluginManager) {
                apply(libs.plugins.google.services.get().pluginId)
                apply(libs.plugins.firebase.crashlytics.get().pluginId)
            }
        }

        extensions.configure<BaseAppModuleExtension> {
            buildTypes {
                debug {
                    manifestPlaceholders["firebase_crashlytics_collection_enabled"] = false
                }

                release {
                    manifestPlaceholders["firebase_crashlytics_collection_enabled"] = true
                }

                getByName("benchmark") {
                    manifestPlaceholders["firebase_crashlytics_collection_enabled"] = false
                }
            }
        }
    }
}
