package com.example.intervalsapp.data.dto

import com.example.intervalsapp.domain.model.Interval
import com.google.gson.annotations.SerializedName

data class IntervalDto(
    @SerializedName("title")
    val title: String,
    @SerializedName("time")
    val time: Long
) {
    fun toDomain(): Interval {
        return Interval(
            title = title,
            durationSec = time
        )
    }
}
