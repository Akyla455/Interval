package com.example.intervalsapp.domain.workout

import com.example.intervalsapp.domain.events.WorkoutPublisher
import com.example.intervalsapp.domain.model.Interval
import com.example.intervalsapp.domain.model.IntervalTimer
import com.example.intervalsapp.domain.model.WorkoutUpdate
import com.example.intervalsapp.domain.platform.SoundPlayer
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutManager @Inject constructor(
    private val publisher: WorkoutPublisher,
    private val soundPlayer: SoundPlayer
) {
    private var job: Job? = null
    private val path = mutableListOf<LatLng>()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    fun start(timer: IntervalTimer) {
        job?.cancel()
        path.clear()
        job = scope.launch {
            var elapsed = 0L
            val total = timer.totalDurationSec

            soundPlayer.singleBeep()

            while (isActive && elapsed < total) {
                delay(1000)
                elapsed++

                val (idx, inInterval) = findInterval(timer.intervals, elapsed)
                if (inInterval == 1L) soundPlayer.singleBeep()

                publisher.publishWorkout(
                    WorkoutUpdate(
                        elapsedSec = elapsed,
                        currentInterval = idx,
                        intervalElapsedSec = inInterval,
                        isFinished = elapsed >= total
                    )
                )
            }

            publisher.publishWorkout(
                WorkoutUpdate(
                    elapsedSec = timer.totalDurationSec,
                    currentInterval = timer.intervals.lastIndex,
                    intervalElapsedSec = timer.intervals.lastOrNull()?.durationSec ?: 0L,
                    isFinished = true
                )
            )

            soundPlayer.doubleBeep()
        }
    }

    fun stop() {
        job?.cancel()
        job = null
        path.clear()
        scope.launch {
            publisher.publishWorkout(
                WorkoutUpdate(
                    elapsedSec = 0L,
                    currentInterval = 0,
                    intervalElapsedSec = 0L,
                    isFinished = false
                )
            )
            publisher.publishLocations(emptyList())
        }
    }

    fun appendLocation(lat: Double, lng: Double) {
        path.add(LatLng(lat, lng))
        scope.launch { publisher.publishLocations(path.toList()) }
    }

    private fun findInterval(list: List<Interval>, elapsed: Long): Pair<Int, Long> {
        var sum = 0L
        list.forEachIndexed { i, interval ->
            val start = sum + 1
            val end = sum + interval.durationSec
            if (elapsed in start..end) return i to (elapsed - sum)
            sum += interval.durationSec
        }
        return list.lastIndex to (list.lastOrNull()?.durationSec ?: 0L)
    }
}