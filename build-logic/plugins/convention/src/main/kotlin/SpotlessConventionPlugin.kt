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

import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.spotless.LineEnding
import com.maximillianleonov.musicmax.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class SpotlessConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.plugins.spotless.get().pluginId)

        extensions.configure<SpotlessExtension> {
            kotlin {
                target("**/*.kt")
                targetExclude("build/**/*.kt", "**/build/**/*.kt", "config/**/*.kt")
                licenseHeaderFile(rootProject.file("config/spotless/copyright.kt"))
            }

            kotlinGradle {
                target("**/*.gradle.kts")
                targetExclude("build/**/*.gradle.kts", "**/build/**/*.gradle.kts")
                licenseHeaderFile(
                    rootProject.file("config/spotless/copyright.kt"),
                    "(plugins |pluginManagement |import |@file)"
                )
            }

            format("xml") {
                target("**/*.xml")
                targetExclude("build/**/*.xml", "**/build/**/*.xml", "config/**/*.xml")
                licenseHeaderFile(rootProject.file("config/spotless/copyright.xml"), "(<[^!?])")
            }

            // https://github.com/diffplug/spotless/issues/1527
            // https://github.com/diffplug/spotless/issues/1644
            lineEndings = LineEnding.PLATFORM_NATIVE
        }
    }
}
