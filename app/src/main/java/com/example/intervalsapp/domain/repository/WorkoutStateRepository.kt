package com.example.intervalsapp.domain.repository

interface WorkoutStateRepository {
    suspend fun saveActiveTimerId(timerId: String)
    suspend fun clearActiveTimerId()
    suspend fun getActiveTimerId(): String?
}