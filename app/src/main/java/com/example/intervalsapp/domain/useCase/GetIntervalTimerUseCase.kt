package com.example.intervalsapp.domain.useCase

import com.example.intervalsapp.domain.repository.IntervalTimerRepository
import javax.inject.Inject

class GetIntervalTimerUseCase @Inject constructor(
    private val repository: IntervalTimerRepository
){
    suspend operator fun invoke(id: String) = repository.getTimer(id)
}