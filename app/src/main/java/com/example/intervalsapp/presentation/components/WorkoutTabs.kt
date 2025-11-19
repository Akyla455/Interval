package com.example.intervalsapp.presentation.components

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

enum class WorkoutTab { Timer, Map }

@Composable
fun WorkoutTabs(selected: WorkoutTab, onSelect: (WorkoutTab) -> Unit) {
    TabRow(selected.ordinal) {
        Tab(
            selected = selected == WorkoutTab.Timer,
            onClick = { onSelect(WorkoutTab.Timer) },
            text = { Text("Таймер") }
        )
        Tab(
            selected = selected == WorkoutTab.Map,
            onClick = { onSelect(WorkoutTab.Map) },
            text = { Text("Карта") }
        )
    }
}