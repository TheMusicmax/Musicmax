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

package com.maximillianleonov.musicmax.core.data.di

import com.maximillianleonov.musicmax.core.data.repository.AlbumRepositoryImpl
import com.maximillianleonov.musicmax.core.data.repository.ArtistRepositoryImpl
import com.maximillianleonov.musicmax.core.data.repository.SettingsRepositoryImpl
import com.maximillianleonov.musicmax.core.data.repository.SongRepositoryImpl
import com.maximillianleonov.musicmax.core.domain.repository.AlbumRepository
import com.maximillianleonov.musicmax.core.domain.repository.ArtistRepository
import com.maximillianleonov.musicmax.core.domain.repository.SettingsRepository
import com.maximillianleonov.musicmax.core.domain.repository.SongRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindSongRepository(songRepositoryImpl: SongRepositoryImpl): SongRepository

    @Binds
    fun bindArtistRepository(artistRepositoryImpl: ArtistRepositoryImpl): ArtistRepository

    @Binds
    fun bindAlbumRepository(albumRepositoryImpl: AlbumRepositoryImpl): AlbumRepository

    @Binds
    fun bindSettingsRepository(settingsRepositoryImpl: SettingsRepositoryImpl): SettingsRepository
}
