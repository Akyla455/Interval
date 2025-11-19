package com.example.intervalsapp.domain.repository

import com.example.intervalsapp.domain.model.IntervalTimer

interface IntervalTimerRepository {
    suspend fun getTimer(id: String): Result<IntervalTimer>
}