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
    `kotlin-dsl`
}

group = "com.maximillianleonov.musicmax.build-logic"

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.spotless.gradle.plugin)
    compileOnly(libs.detekt.gradle.plugin)

    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "musicmax.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "musicmax.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "musicmax.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "musicmax.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidFeature") {
            id = "musicmax.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("hilt") {
            id = "musicmax.hilt"
            implementationClass = "HiltConventionPlugin"
        }
        register("androidHilt") {
            id = "musicmax.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidBenchmark") {
            id = "musicmax.android.benchmark"
            implementationClass = "AndroidBenchmarkConventionPlugin"
        }
        register("androidLint") {
            id = "musicmax.android.lint"
            implementationClass = "AndroidLintConventionPlugin"
        }
        register("androidSigningConfig") {
            id = "musicmax.android.signing-config"
            implementationClass = "AndroidSigningConfigConventionPlugin"
        }
        register("spotless") {
            id = "musicmax.spotless"
            implementationClass = "SpotlessConventionPlugin"
        }
        register("detekt") {
            id = "musicmax.detekt"
            implementationClass = "DetektConventionPlugin"
        }
        register("firebaseConfig") {
            id = "musicmax.firebase-config"
            implementationClass = "FirebaseConfigConventionPlugin"
        }
    }
}
