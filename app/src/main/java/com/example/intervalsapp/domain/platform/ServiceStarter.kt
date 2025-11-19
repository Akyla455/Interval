package com.example.intervalsapp.domain.platform

import com.example.intervalsapp.domain.model.IntervalTimer

interface ServiceStarter {
    fun startServiceWithTimer(timer: IntervalTimer)
    fun stopService()
}