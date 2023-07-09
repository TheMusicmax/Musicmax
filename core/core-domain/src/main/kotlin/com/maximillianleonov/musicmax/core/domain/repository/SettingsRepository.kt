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

package com.maximillianleonov.musicmax.core.domain.repository

import com.maximillianleonov.musicmax.core.model.DarkThemeConfig
import com.maximillianleonov.musicmax.core.model.PlaybackMode
import com.maximillianleonov.musicmax.core.model.SortBy
import com.maximillianleonov.musicmax.core.model.SortOrder
import com.maximillianleonov.musicmax.core.model.UserData
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val userData: Flow<UserData>

    val repoUrl: String
    val privacyPolicyUrl: String
    val version: String

    suspend fun setPlayingQueueIds(playingQueueIds: List<String>)
    suspend fun setPlayingQueueIndex(playingQueueIndex: Int)
    suspend fun setPlaybackMode(playbackMode: PlaybackMode)
    suspend fun setSortOrder(sortOrder: SortOrder)
    suspend fun setSortBy(sortBy: SortBy)
    suspend fun toggleFavoriteSong(id: String, isFavorite: Boolean)
    suspend fun setDynamicColor(useDynamicColor: Boolean)
    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)
}
