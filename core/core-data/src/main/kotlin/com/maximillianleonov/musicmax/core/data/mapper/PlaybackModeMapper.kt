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

package com.maximillianleonov.musicmax.core.data.mapper

import com.maximillianleonov.musicmax.core.domain.model.PlaybackModeModel
import com.maximillianleonov.musicmax.core.model.PlaybackMode

internal fun PlaybackMode.asPlaybackModeModel() = when (this) {
    PlaybackMode.REPEAT -> PlaybackModeModel.REPEAT
    PlaybackMode.REPEAT_ONE -> PlaybackModeModel.REPEAT_ONE
    PlaybackMode.SHUFFLE -> PlaybackModeModel.SHUFFLE
}

internal fun PlaybackModeModel.asPlaybackMode() = when (this) {
    PlaybackModeModel.REPEAT -> PlaybackMode.REPEAT
    PlaybackModeModel.REPEAT_ONE -> PlaybackMode.REPEAT_ONE
    PlaybackModeModel.SHUFFLE -> PlaybackMode.SHUFFLE
}
