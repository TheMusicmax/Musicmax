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

package com.maximillianleonov.musicmax.core.database.di

import android.content.Context
import androidx.room.Room
import com.maximillianleonov.musicmax.core.database.MusicmaxDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideMusicmaxDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        MusicmaxDatabase::class.java,
        MUSICMAX_DATABASE
    ).build()

    @Provides
    fun provideSongDao(musicmaxDatabase: MusicmaxDatabase) = musicmaxDatabase.songDao
}

private const val MUSICMAX_DATABASE = "musicmax.db"
