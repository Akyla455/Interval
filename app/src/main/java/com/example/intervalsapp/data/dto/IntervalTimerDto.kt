package com.example.intervalsapp.data.dto

import com.example.intervalsapp.domain.model.IntervalTimer
import com.google.gson.annotations.SerializedName

data class IntervalTimerResponse(
    @SerializedName("timer")
    val timer: IntervalTimerDto
)

data class IntervalTimerDto(
    @SerializedName("timer_id")
    val id: Long,

    @SerializedName("title")
    val title: String,

    @SerializedName("total_time")
    val totalTime: Long,

    @SerializedName("intervals")
    val intervals: List<IntervalDto>
) {
    fun toDomain(): IntervalTimer {
        return IntervalTimer(
            id = id,
            name = title,
            intervals = intervals.map { it.toDomain() },
            totalDurationSec = totalTime
        )
    }
}


