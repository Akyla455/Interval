package com.example.intervalsapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.intervalsapp.domain.model.Interval

@Composable
fun IntervalBars(
    modifier: Modifier = Modifier,
    intervals: List<Interval>,
    currentIndex: Int,
) {
    val total = intervals.sumOf { it.durationSec }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFE0E0E0))
    ) {

        intervals.forEachIndexed { index, interval ->

            val weight = interval.durationSec.toFloat() / total.toFloat()
            val isCurrent = index == currentIndex

            Box(
                modifier = Modifier
                    .weight(weight)
                    .fillMaxHeight()
                    .padding(horizontal = if (index == intervals.lastIndex) 0.dp else 1.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        when {
                            isCurrent -> Color(0xFF4CAF50)
                            index < currentIndex -> Color(0xFF81C784)
                            else -> Color(0xFFBDBDBD)
                        }
                    )
            )
        }
    }
}