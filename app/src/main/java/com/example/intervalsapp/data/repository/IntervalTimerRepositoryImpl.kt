package com.example.intervalsapp.data.repository

import com.example.intervalsapp.data.remote.ApiService
import com.example.intervalsapp.domain.model.Interval
import com.example.intervalsapp.domain.model.IntervalTimer
import com.example.intervalsapp.domain.repository.IntervalTimerRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result

@Singleton
class IntervalTimerRepositoryImpl @Inject constructor(
    private val api: ApiService
) : IntervalTimerRepository {
    override suspend fun getTimer(id: String): Result<IntervalTimer> {
        return try {
            val response = api.getIntervalTimer(id)
            if (!response.isSuccessful) {
                return Result.failure(Exception("HTTP ${response.code()} ${response.message()}"))
            }
            val body = response.body()
            if (body == null) return Result.failure(Exception("Response body is null"))
            Result.success(body.timer.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}