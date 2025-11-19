package com.example.intervalsapp.domain.events

import com.example.intervalsapp.domain.model.WorkoutUpdate
import com.google.android.gms.maps.model.LatLng

interface WorkoutPublisher {
    suspend fun publishWorkout(update: WorkoutUpdate)
    suspend fun publishLocations(locations: List<LatLng>)
}