package com.example.intervalsapp.presentation.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.intervalsapp.domain.model.IntervalTimer
import com.example.intervalsapp.domain.model.WorkoutUpdate
import com.example.intervalsapp.presentation.WorkoutViewModel
import com.example.intervalsapp.presentation.components.MapTabContent
import com.example.intervalsapp.presentation.components.TimerTabContent
import com.example.intervalsapp.presentation.components.WorkoutBottomBar
import com.example.intervalsapp.presentation.components.WorkoutTab
import com.example.intervalsapp.presentation.components.WorkoutTabs
import com.google.android.gms.maps.model.LatLng


@Composable
fun WorkoutScreen(
    navController: NavHostController,
    viewModel: WorkoutViewModel = hiltViewModel()
) {
    val timer by viewModel.timer.collectAsStateWithLifecycle()
    val workout by viewModel.workout.collectAsStateWithLifecycle()
    val locations by viewModel.locations.collectAsStateWithLifecycle()

    val permissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isCoarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        val isFineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
    }

    LaunchedEffect(Unit) {
        val permissionsToRequest = mutableListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        permissionsLauncher.launch(permissionsToRequest.toTypedArray())
    }

    if (timer == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        WorkoutContent(
            timer = timer!!,
            workout = workout,
            locations = locations,
            onStart = viewModel::start,
            onStop = viewModel::stop,
            onBack = navController::popBackStack
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WorkoutContent(
    timer: IntervalTimer,
    workout: WorkoutUpdate?,
    locations: List<LatLng>,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onBack: () -> Unit
) {
    var tab by remember { mutableStateOf(WorkoutTab.Timer) }
    val state = WorkoutUiState.from(timer, workout)

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text(timer.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            WorkoutBottomBar(
                isRunning = !state.isFinished && state.elapsedTotalSec > 0,
                onStart = onStart,
                onStop = onStop
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()

        ) {
            WorkoutTabs(
                selected = tab,
                onSelect = { tab = it }
            )

            when (tab) {
                WorkoutTab.Timer -> TimerTabContent(state)
                WorkoutTab.Map -> MapTabContent(locations)
            }
        }
    }
}