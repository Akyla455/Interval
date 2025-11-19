package com.example.intervalsapp.domain.useCase

import com.example.intervalsapp.domain.platform.ServiceStarter
import javax.inject.Inject

class StopWorkoutUseCase @Inject constructor(
    private val starter: ServiceStarter
) {
    operator fun invoke() = starter.stopService()
}