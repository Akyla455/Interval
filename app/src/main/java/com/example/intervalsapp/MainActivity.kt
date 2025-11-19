package com.example.intervalsapp

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.intervalsapp.domain.repository.WorkoutStateRepository
import com.example.intervalsapp.presentation.WorkoutService
import com.example.intervalsapp.presentation.navigation.NavGraph
import com.example.intervalsapp.presentation.theme.IntervalsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var workoutStateRepository: WorkoutStateRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        var startDestination = "start"

        if (isWorkoutServiceRunning(this)) {
            val activeId = runBlocking {
                workoutStateRepository.getActiveTimerId()
            }
            if (activeId != null) {
                startDestination = "workout/$activeId"
            }
        }
        setContent {
            val navController = rememberNavController()
            IntervalsAppTheme {
                NavGraph(
                    navController = navController,
                    startDestination = startDestination
                )
            }
        }
    }
    private fun isWorkoutServiceRunning(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        @Suppress("DEPRECATION")
        for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
            if (WorkoutService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }
}
