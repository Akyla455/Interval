package com.example.intervalsapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Interval(
    val title: String,
    val durationSec: Long
) : Parcelable
