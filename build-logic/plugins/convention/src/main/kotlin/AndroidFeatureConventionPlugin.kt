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

import com.maximillianleonov.musicmax.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("musicmax.android.library")
            apply("musicmax.android.library.compose")
            apply("musicmax.android.hilt")
        }

        dependencies {
            add("implementation", project(":core:core-ui"))

            add("implementation", libs.androidx.lifecycle.runtime.compose)
            add("implementation", libs.androidx.lifecycle.viewmodel.compose)
            add("implementation", libs.androidx.hilt.navigation.compose)
            add("implementation", libs.kotlinx.coroutines.android)
        }
    }
}
