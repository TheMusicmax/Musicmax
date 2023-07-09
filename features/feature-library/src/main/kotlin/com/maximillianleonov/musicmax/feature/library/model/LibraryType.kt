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

package com.maximillianleonov.musicmax.feature.library.model

enum class LibraryType(val value: String) {
    Artist(LIBRARY_TYPE_ARTIST),
    Album(LIBRARY_TYPE_ALBUM),
    Folder(LIBRARY_TYPE_FOLDER);

    companion object {
        private val libraryTypes = values().associateBy(LibraryType::value)
        operator fun get(value: String) =
            checkNotNull(libraryTypes[value]) { "Invalid library type $value." }
    }
}

private const val LIBRARY_TYPE_ARTIST = "artist"
private const val LIBRARY_TYPE_ALBUM = "album"
private const val LIBRARY_TYPE_FOLDER = "folder"
