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

package com.maximillianleonov.musicmax.core.datastore.util

import androidx.datastore.preferences.core.intPreferencesKey

internal object Constants {
    private const val PLAYING_QUEUE_INDEX_NAME = "playing_queue_index"
    internal val PLAYING_QUEUE_INDEX = intPreferencesKey(PLAYING_QUEUE_INDEX_NAME)
}
