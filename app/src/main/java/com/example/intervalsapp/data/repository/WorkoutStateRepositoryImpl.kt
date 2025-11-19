package com.example.intervalsapp.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.intervalsapp.domain.repository.WorkoutStateRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "workout_prefs")

@Singleton
class WorkoutStateRepositoryImpl @Inject constructor(
    @param:ApplicationContext
    private val context: Context
) : WorkoutStateRepository {

    private val ACTIVE_TIMER_KEY = stringPreferencesKey("active_timer_id")

    override suspend fun saveActiveTimerId(timerId: String) {
        context.dataStore.edit { prefs ->
            prefs[ACTIVE_TIMER_KEY] = timerId
        }
    }

    override suspend fun clearActiveTimerId() {
        context.dataStore.edit { prefs ->
            prefs.remove(ACTIVE_TIMER_KEY)
        }
    }

    override suspend fun getActiveTimerId(): String? {
        return context.dataStore.data
            .map { prefs -> prefs[ACTIVE_TIMER_KEY] }
            .first()
    }
}