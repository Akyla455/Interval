package com.example.intervalsapp.data.platform

import android.content.Context
import android.content.Intent
import com.example.intervalsapp.domain.model.IntervalTimer
import com.example.intervalsapp.domain.platform.ServiceStarter
import com.example.intervalsapp.presentation.WorkoutService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceStarterImpl @Inject constructor(
    @param:ApplicationContext
    private val context: Context
): ServiceStarter {
    override fun startServiceWithTimer(timer: IntervalTimer) {
        val intent = Intent(context, WorkoutService::class.java).apply {
            action = "START"
            putExtra("timer", timer)
        }
        context.startForegroundService(intent)
    }

    override fun stopService() {
        val intent = Intent(context, WorkoutService::class.java).apply {
            action = "STOP"
        }
        context.startService(intent)
    }
}