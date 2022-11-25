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

package com.maximillianleonov.musicmax.core.database.util

internal object Constants {
    internal object Tables {
        internal const val SONGS = "songs"
        internal const val ARTISTS = "artists"
        internal const val ALBUMS = "albums"
        internal const val PLAYING_QUEUE = "playing_queue"
    }

    internal object Fields {
        internal const val ID = "id"
        internal const val MEDIA_ID = "media_id"
        internal const val ARTIST_ID = "artist_id"
        internal const val ALBUM_ID = "album_id"
        internal const val MEDIA_URI = "media_uri"
        internal const val ARTWORK_URI = "artwork_uri"
        internal const val TITLE = "title"
        internal const val ARTIST = "artist"
        internal const val ALBUM = "album"
        internal const val NAME = "name"
        internal const val NUMBER_OF_SONGS = "number_of_songs"
    }
}
