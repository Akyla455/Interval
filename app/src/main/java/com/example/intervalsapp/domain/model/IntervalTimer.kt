package com.example.intervalsapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IntervalTimer(
    val id: Long,
    val name: String,
    val intervals: List<Interval>,
    val totalDurationSec: Long
) : Parcelable