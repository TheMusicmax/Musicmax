/*
 * Copyright 2023 Maximillian Leonov
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
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.File
import java.util.Properties

class AdMobConfigProviderConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val properties = Properties()
        val file = rootProject.file("admob.properties")
        if (file.exists() && file.isFile) {
            file.inputStream().use { input ->
                properties.load(input)
            }
        }

        extensions.configure<BaseAppModuleExtension> {
            val adMobConfigMap = getAdMobConfigMap(file, properties)
            val appId = adMobConfigMap.getValue("appId")
            val songsBannerUnitId = adMobConfigMap.getValue("songsBannerUnitId")
            val artistsBannerUnitId = adMobConfigMap.getValue("artistsBannerUnitId")

            buildTypes {
                debug {
                    manifestPlaceholders["admob_app_id"] = TEST_APP_ID
                    buildConfigField(
                        "String",
                        "ADMOB_SONGS_BANNER_UNIT_ID",
                        "\"$TEST_BANNER_ID\""
                    )
                    buildConfigField(
                        "String",
                        "ADMOB_ARTISTS_BANNER_UNIT_ID",
                        "\"$TEST_BANNER_ID\""
                    )
                }

                release {
                    manifestPlaceholders["admob_app_id"] = appId
                    buildConfigField(
                        "String",
                        "ADMOB_SONGS_BANNER_UNIT_ID",
                        "\"$songsBannerUnitId\""
                    )
                    buildConfigField(
                        "String",
                        "ADMOB_ARTISTS_BANNER_UNIT_ID",
                        "\"$artistsBannerUnitId\""
                    )
                }

                getByName("benchmark") {
                    manifestPlaceholders["admob_app_id"] = TEST_APP_ID
                    buildConfigField(
                        "String",
                        "ADMOB_SONGS_BANNER_UNIT_ID",
                        "\"$TEST_BANNER_ID\""
                    )
                    buildConfigField(
                        "String",
                        "ADMOB_ARTISTS_BANNER_UNIT_ID",
                        "\"$TEST_BANNER_ID\""
                    )
                }
            }
        }
    }
}

private fun getAdMobConfigMap(file: File, properties: Properties) =
    if (file.exists()) {
        mapOf(
            "appId" to properties.getProperty("appId"),
            "songsBannerUnitId" to properties.getProperty("songsBannerUnitId"),
            "artistsBannerUnitId" to properties.getProperty("artistsBannerUnitId")
        ).onEach { (key, value) ->
            requireNotNull(value) { "$key is null." }
        }
    } else {
        mapOf(
            "appId" to TEST_APP_ID,
            "songsBannerUnitId" to TEST_BANNER_ID,
            "artistsBannerUnitId" to TEST_BANNER_ID
        )
    }

private const val TEST_APP_ID = "ca-app-pub-3940256099942544~3347511713"
private const val TEST_BANNER_ID = "ca-app-pub-3940256099942544/6300978111"
