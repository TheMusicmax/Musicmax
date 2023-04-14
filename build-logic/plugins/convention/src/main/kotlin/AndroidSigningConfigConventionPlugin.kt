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

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.android.build.gradle.internal.dsl.SigningConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.util.Properties

class AndroidSigningConfigConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val keystoreProperties = Properties()
        val keystorePropertiesFile = rootProject.file("keystore.properties")
        if (keystorePropertiesFile.exists() && keystorePropertiesFile.isFile) {
            keystorePropertiesFile.inputStream().use { input ->
                keystoreProperties.load(input)
            }
        }

        extensions.configure<BaseAppModuleExtension> {
            val signingConfig = if (keystorePropertiesFile.exists()) {
                createSigningConfigFromProperties(
                    target = this@with,
                    name = "release",
                    properties = keystoreProperties
                )
            } else {
                signingConfigs.getByName("debug")
            }

            defaultConfig {
                buildTypes {
                    release {
                        this.signingConfig = signingConfig
                    }
                }
            }
        }
    }
}

private fun BaseAppModuleExtension.createSigningConfigFromProperties(
    target: Project,
    name: String,
    properties: Properties
): SigningConfig {
    val keystore = mapOf(
        "keyAlias" to properties.getProperty("keyAlias"),
        "keyPassword" to properties.getProperty("keyPassword"),
        "storeFile" to properties.getProperty("storeFile"),
        "storePassword" to properties.getProperty("storePassword")
    )

    keystore.forEach { (key, value) ->
        requireNotNull(value) { "$key is null." }
    }

    return signingConfigs.create(name) {
        keyAlias = keystore.getValue("keyAlias")
        keyPassword = keystore.getValue("keyPassword")
        storeFile = target.file(keystore.getValue("storeFile"))
        storePassword = keystore.getValue("storePassword")
    }
}
