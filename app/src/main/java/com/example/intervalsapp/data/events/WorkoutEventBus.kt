package com.example.intervalsapp.data.events

import com.example.intervalsapp.domain.events.WorkoutObserver
import com.example.intervalsapp.domain.events.WorkoutPublisher
import com.example.intervalsapp.domain.model.WorkoutUpdate
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutEventBus @Inject constructor() : WorkoutPublisher, WorkoutObserver {
    private val _workout = MutableSharedFlow<WorkoutUpdate>(replay = 1)
    private val _locations = MutableSharedFlow<List<LatLng>>(replay = 1)

    override suspend fun publishWorkout(update: WorkoutUpdate) {
        _workout.emit(update)
    }

    override suspend fun publishLocations(locations: List<LatLng>) {
        _locations.emit(locations)
    }

    override fun observeWorkout(): SharedFlow<WorkoutUpdate> = _workout.asSharedFlow()
    override fun observeLocations(): SharedFlow<List<LatLng>> = _locations.asSharedFlow()
}