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

package com.maximillianleonov.musicmax.core.domain.repository

import com.maximillianleonov.musicmax.core.domain.model.PlaybackModeModel
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val playingQueueIds: Flow<List<String>>
    val playingQueueIndex: Flow<Int>
    val playbackMode: Flow<PlaybackModeModel>
    val favoriteSongs: Flow<Set<String>>

    val repoUrl: String
    val privacyPolicyUrl: String
    val version: String

    suspend fun setPlayingQueueIds(playingQueueIds: List<String>)
    suspend fun setPlayingQueueIndex(playingQueueIndex: Int)
    suspend fun setPlaybackMode(playbackMode: PlaybackModeModel)
    suspend fun toggleFavoriteSong(id: String, isFavorite: Boolean)
}
