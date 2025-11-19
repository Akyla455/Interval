package com.example.intervalsapp.domain.useCase

import com.example.intervalsapp.domain.events.WorkoutObserver
import com.example.intervalsapp.domain.model.WorkoutUpdate
import com.example.intervalsapp.domain.repository.IntervalTimerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveWorkoutUseCase @Inject constructor(
    private val observer: WorkoutObserver
) {
    operator fun invoke(): Flow<WorkoutUpdate> = observer.observeWorkout()
}