package com.example.intervalsapp.data.remote

import com.example.intervalsapp.data.dto.IntervalTimerDto
import com.example.intervalsapp.data.dto.IntervalTimerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiService {
    @GET("api/v2/interval-timers/{id}")
    suspend fun getIntervalTimer(
        @Path("id") id: String
    ): Response<IntervalTimerResponse>
}