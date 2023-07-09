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

package com.maximillianleonov.musicmax.core.datastore.mapper

import com.maximillianleonov.musicmax.core.datastore.SortOrderProto
import com.maximillianleonov.musicmax.core.model.SortOrder

internal fun SortOrder.asSortOrderProto() = when (this) {
    SortOrder.ASCENDING -> SortOrderProto.SORT_ORDER_ASCENDING
    SortOrder.DESCENDING -> SortOrderProto.SORT_ORDER_DESCENDING
}

internal fun SortOrderProto.asSortOrder() = when (this) {
    SortOrderProto.SORT_ORDER_ASCENDING -> SortOrder.ASCENDING

    SortOrderProto.UNRECOGNIZED,
    SortOrderProto.SORT_ORDER_UNSPECIFIED,
    SortOrderProto.SORT_ORDER_DESCENDING -> SortOrder.DESCENDING
}
