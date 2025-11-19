package com.example.intervalsapp.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.intervalsapp.presentation.ui.WorkoutUiState

@Composable
fun TimerTabContent(state: WorkoutUiState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

//        IntervalBars(
//            intervals = state.intervalsList,
//            currentIndex = state.currentIndex,
//            modifier = Modifier.padding(bottom = 24.dp)
//        )

        TimerBigComponent(
            intervalTitle = state.currentIntervalTitle,
            totalTime = state.currentIntervalTotal,
            elapsed = state.currentIntervalElapsed
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = buildString {
                append("Общее время: ")
                append(formatSeconds(state.elapsedTotalSec))
                append(" / ")
                append(formatSeconds(state.totalDurationSec))
            },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

private fun formatSeconds(sec: Long): String {
    val m = sec / 60
    val s = sec % 60
    return "%02d:%02d".format(m, s)
}