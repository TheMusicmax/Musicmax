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

package com.maximillianleonov.musicmax.core.data.repository

import com.maximillianleonov.musicmax.core.data.util.Constants
import com.maximillianleonov.musicmax.core.data.util.MusicmaxVersionProvider
import com.maximillianleonov.musicmax.core.datastore.PreferencesDataSource
import com.maximillianleonov.musicmax.core.domain.repository.SettingsRepository
import com.maximillianleonov.musicmax.core.model.DarkThemeConfig
import com.maximillianleonov.musicmax.core.model.PlaybackMode
import com.maximillianleonov.musicmax.core.model.SortBy
import com.maximillianleonov.musicmax.core.model.SortOrder
import com.maximillianleonov.musicmax.core.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource,
    versionProvider: MusicmaxVersionProvider
) : SettingsRepository {
    override val userData: Flow<UserData> = preferencesDataSource.userData

    override val repoUrl = Constants.Urls.MUSICMAX_REPO_URL
    override val privacyPolicyUrl = Constants.Urls.PRIVACY_POLICY_URL
    override val version = versionProvider.version

    override suspend fun setPlayingQueueIds(playingQueueIds: List<String>) =
        preferencesDataSource.setPlayingQueueIds(playingQueueIds)

    override suspend fun setPlayingQueueIndex(playingQueueIndex: Int) =
        preferencesDataSource.setPlayingQueueIndex(playingQueueIndex)

    override suspend fun setPlaybackMode(playbackMode: PlaybackMode) =
        preferencesDataSource.setPlaybackMode(playbackMode)

    override suspend fun setSortOrder(sortOrder: SortOrder) =
        preferencesDataSource.setSortOrder(sortOrder)

    override suspend fun setSortBy(sortBy: SortBy) = preferencesDataSource.setSortBy(sortBy)

    override suspend fun toggleFavoriteSong(id: String, isFavorite: Boolean) =
        preferencesDataSource.toggleFavoriteSong(id, isFavorite)

    override suspend fun setDynamicColor(useDynamicColor: Boolean) =
        preferencesDataSource.setDynamicColor(useDynamicColor)

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) =
        preferencesDataSource.setDarkThemeConfig(darkThemeConfig)
}
