package com.example.intervalsapp.presentation.ui

import com.example.intervalsapp.domain.model.IntervalTimer
import com.example.intervalsapp.domain.model.WorkoutUpdate

data class WorkoutUiState(
    val totalDurationSec: Long,
    val elapsedTotalSec: Long,
    val currentIndex: Int,
    val currentIntervalTitle: String,
    val currentIntervalElapsed: Long,
    val currentIntervalTotal: Long,
    val isFinished: Boolean
) {
    companion object {
        fun from(timer: IntervalTimer, upd: WorkoutUpdate?): WorkoutUiState {
            if (upd == null) {
                return WorkoutUiState(
                    totalDurationSec = timer.totalDurationSec,
                    elapsedTotalSec = 0,
                    currentIndex = 0,
                    currentIntervalTitle = timer.intervals.first().title,
                    currentIntervalElapsed = 0,
                    currentIntervalTotal = timer.intervals.first().durationSec,
                    isFinished = false
                )
            }

            val idx = upd.currentInterval.coerceIn(0, timer.intervals.lastIndex)
            val interval = timer.intervals[idx]

            return WorkoutUiState(
                totalDurationSec = timer.totalDurationSec,
                elapsedTotalSec = upd.elapsedSec,
                currentIndex = idx,
                currentIntervalTitle = interval.title,
                currentIntervalElapsed = upd.intervalElapsedSec,
                currentIntervalTotal = interval.durationSec,
                isFinished = upd.isFinished
            )
        }
    }
}