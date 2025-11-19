package com.example.intervalsapp.domain.useCase

import com.example.intervalsapp.domain.model.IntervalTimer
import com.example.intervalsapp.domain.platform.ServiceStarter
import javax.inject.Inject

class StartWorkoutUseCase @Inject constructor(
    private val starter: ServiceStarter
) {
    operator fun invoke(timer: IntervalTimer) = starter.startServiceWithTimer(timer)
}