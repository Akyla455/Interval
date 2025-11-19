package com.example.intervalsapp.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapTabContent(track: List<LatLng>) {
    val defaultCameraPosition = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 1f)
    val cameraPositionState = rememberCameraPositionState {
        position = defaultCameraPosition
    }

    LaunchedEffect(track) {
        track.lastOrNull()?.let { lastPoint ->
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(lastPoint, 17f),
                durationMs = 1000
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(myLocationButtonEnabled = false)
        ) {
            if (track.isNotEmpty()) {
                Polyline(
                    points = track,
                    color = androidx.compose.ui.graphics.Color.Blue,
                    width = 12f
                )
            }

            track.firstOrNull()?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Старт"
                )
            }
            track.lastOrNull()?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Вы здесь"
                )
            }
        }
    }
}