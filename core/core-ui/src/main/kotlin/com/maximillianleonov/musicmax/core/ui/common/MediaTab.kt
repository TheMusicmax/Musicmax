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

package com.maximillianleonov.musicmax.core.ui.common

import androidx.annotation.StringRes
import com.maximillianleonov.musicmax.core.ui.R

internal enum class MediaTab(@StringRes val titleResource: Int) {
    Songs(titleResource = R.string.songs),
    Artists(titleResource = R.string.artists),
    Albums(titleResource = R.string.albums),
    Folders(titleResource = R.string.folders)
}
