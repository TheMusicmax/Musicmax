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

package com.maximillianleonov.musicmax.sync.work.initializers

import android.content.Context
import androidx.startup.Initializer
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import androidx.work.WorkManagerInitializer
import com.maximillianleonov.musicmax.sync.work.workers.SyncWorker

/**
 * Registers work to sync the data layer periodically on app startup.
 */
class SyncInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        WorkManager.getInstance(context).apply {
            // Run sync on app startup and ensure only one sync worker runs at any time.
            enqueueUniqueWork(
                SyncWorkName,
                ExistingWorkPolicy.KEEP,
                SyncWorker.startUpSyncWork()
            )
        }
    }

    override fun dependencies() = listOf(WorkManagerInitializer::class.java)
}

// This name should not be changed otherwise the app may have concurrent sync requests running.
internal const val SyncWorkName = "SyncWorkName"
