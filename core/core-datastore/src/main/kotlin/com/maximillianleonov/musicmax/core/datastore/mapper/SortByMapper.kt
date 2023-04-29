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

package com.maximillianleonov.musicmax.core.datastore.mapper

import com.maximillianleonov.musicmax.core.datastore.SortByProto
import com.maximillianleonov.musicmax.core.model.SortBy

internal fun SortBy.asSortByProto() = when (this) {
    SortBy.TITLE -> SortByProto.SORT_BY_TITLE
    SortBy.ARTIST -> SortByProto.SORT_BY_ARTIST
    SortBy.ALBUM -> SortByProto.SORT_BY_ALBUM
    SortBy.DURATION -> SortByProto.SORT_BY_DURATION
    SortBy.DATE -> SortByProto.SORT_BY_DATE
}

internal fun SortByProto.asSortBy() = when (this) {
    SortByProto.SORT_BY_TITLE -> SortBy.TITLE
    SortByProto.SORT_BY_ARTIST -> SortBy.ARTIST
    SortByProto.SORT_BY_ALBUM -> SortBy.ALBUM
    SortByProto.SORT_BY_DURATION -> SortBy.DURATION

    SortByProto.UNRECOGNIZED,
    SortByProto.SORT_BY_UNSPECIFIED,
    SortByProto.SORT_BY_DATE -> SortBy.DATE
}
