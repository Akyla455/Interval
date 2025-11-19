package com.example.intervalsapp.domain.useCase

import com.example.intervalsapp.domain.events.WorkoutObserver
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveLocationUseCase @Inject constructor(
    private val observer: WorkoutObserver
) {
    operator fun invoke(): Flow<List<LatLng>> = observer.observeLocations()
}