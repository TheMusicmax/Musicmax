package com.maximillianleonov.musicmax.core.datastore

import androidx.datastore.core.DataStore
import com.maximillianleonov.musicmax.core.model.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    val userData = userPreferences.data.map { preferences ->
        UserData(
            playingQueueIndex = preferences.playingQueueIndex
        )
    }

    suspend fun setPlayingQueueIndex(playingQueueIndex: Int) {
        userPreferences.updateData {
            it.copy {
                this.playingQueueIndex = playingQueueIndex
            }
        }
    }
}
