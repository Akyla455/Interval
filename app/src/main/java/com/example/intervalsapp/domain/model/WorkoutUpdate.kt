package com.example.intervalsapp.domain.model

data class WorkoutUpdate(
    val elapsedSec: Long,
    val currentInterval: Int,
    val intervalElapsedSec: Long,
    val isFinished: Boolean
)
