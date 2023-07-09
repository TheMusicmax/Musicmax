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

import com.maximillianleonov.musicmax.libs
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.plugins.detekt.get().pluginId)

        extensions.configure<DetektExtension> {
            toolVersion = libs.versions.detekt.get()
            source.setFrom(files(rootDir))
            config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
            buildUponDefaultConfig = true
            parallel = true
        }

        dependencies {
            add("detektPlugins", libs.detekt.formatting)
            add("detektPlugins", libs.kode.detekt.rules.compose)
            add("detektPlugins", libs.twitter.compose.rules.detekt)
        }

        tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
            exclude("**/build/**")
            exclude("config/**")
        }
    }
}
