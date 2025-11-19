package com.example.intervalsapp.domain.events

import com.example.intervalsapp.domain.model.WorkoutUpdate
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface WorkoutObserver {
    fun observeWorkout(): Flow<WorkoutUpdate>
    fun observeLocations(): Flow<List<LatLng>>
}