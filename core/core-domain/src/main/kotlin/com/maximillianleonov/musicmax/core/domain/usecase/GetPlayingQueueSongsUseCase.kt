/*
 * Copyright 2023 Afig Aliyev
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

package com.maximillianleonov.musicmax.core.domain.usecase

import com.maximillianleonov.musicmax.core.domain.repository.MediaRepository
import com.maximillianleonov.musicmax.core.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPlayingQueueSongsUseCase @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke() = combine(
        mediaRepository.songs,
        settingsRepository.userData.map { it.playingQueueIds }
    ) { songs, playingQueueIds ->
        playingQueueIds.mapNotNull { playingQueueId -> songs.find { it.mediaId == playingQueueId } }
    }
}
