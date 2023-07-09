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

package com.maximillianleonov.musicmax.core.media.service.util

import androidx.media3.common.C
import androidx.media3.common.Player
import com.maximillianleonov.musicmax.core.media.common.MediaConstants.DEFAULT_DURATION_MS
import com.maximillianleonov.musicmax.core.model.PlaybackState

internal fun Int.asPlaybackState() = when (this) {
    Player.STATE_IDLE -> PlaybackState.IDLE
    Player.STATE_BUFFERING -> PlaybackState.BUFFERING
    Player.STATE_READY -> PlaybackState.READY
    Player.STATE_ENDED -> PlaybackState.ENDED
    else -> error("Invalid playback state.")
}

internal fun Long.orDefaultTimestamp() = takeIf { it != C.TIME_UNSET } ?: DEFAULT_DURATION_MS
