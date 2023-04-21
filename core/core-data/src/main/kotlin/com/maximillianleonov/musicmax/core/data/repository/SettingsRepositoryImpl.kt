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

package com.maximillianleonov.musicmax.core.data.repository

import com.maximillianleonov.musicmax.core.data.mapper.asPlaybackMode
import com.maximillianleonov.musicmax.core.data.mapper.asPlaybackModeModel
import com.maximillianleonov.musicmax.core.data.util.Constants
import com.maximillianleonov.musicmax.core.data.util.MusicmaxVersionProvider
import com.maximillianleonov.musicmax.core.datastore.PreferencesDataSource
import com.maximillianleonov.musicmax.core.domain.model.PlaybackModeModel
import com.maximillianleonov.musicmax.core.domain.repository.SettingsRepository
import com.maximillianleonov.musicmax.core.model.PlaybackMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource,
    versionProvider: MusicmaxVersionProvider
) : SettingsRepository {
    override val playingQueueIds: Flow<List<String>> =
        preferencesDataSource.userData.map { it.playingQueueIds }

    override val playingQueueIndex: Flow<Int> =
        preferencesDataSource.userData.map { it.playingQueueIndex }

    override val playbackMode: Flow<PlaybackModeModel> =
        preferencesDataSource.userData
            .map { it.playbackMode }
            .map(PlaybackMode::asPlaybackModeModel)

    override val favoriteSongs: Flow<Set<String>> =
        preferencesDataSource.userData.map { it.favoriteSongs }

    override val repoUrl = Constants.Urls.MUSICMAX_REPO_URL
    override val privacyPolicyUrl = Constants.Urls.PRIVACY_POLICY_URL
    override val version = versionProvider.version

    override suspend fun setPlayingQueueIds(playingQueueIds: List<String>) =
        preferencesDataSource.setPlayingQueueIds(playingQueueIds)

    override suspend fun setPlayingQueueIndex(playingQueueIndex: Int) =
        preferencesDataSource.setPlayingQueueIndex(playingQueueIndex)

    override suspend fun setPlaybackMode(playbackMode: PlaybackModeModel) =
        preferencesDataSource.setPlaybackMode(playbackMode.asPlaybackMode())

    override suspend fun toggleFavoriteSong(id: String, isFavorite: Boolean) =
        preferencesDataSource.toggleFavoriteSong(id, isFavorite)
}
