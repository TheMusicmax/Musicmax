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

pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "Musicmax"

include(":app")
include(":benchmark")
include(":core:core-common")
include(":core:core-data")
include(":core:core-datastore")
include(":core:core-mediastore")
include(":core:core-domain")
include(":core:core-ui")
include(":core:core-designsystem")
include(":core:core-model")
include(":core:core-media-common")
include(":core:core-media-service")
include(":core:core-media-notification")
include(":core:core-permission")
include(":features:feature-home")
include(":features:feature-search")
include(":features:feature-favorite")
include(":features:feature-settings")
include(":features:feature-player")
include(":features:feature-library")
