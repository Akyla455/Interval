package com.example.intervalsapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.intervalsapp.presentation.ui.StartScreen
import com.example.intervalsapp.presentation.ui.WorkoutScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {

    NavHost(
        navController,
        startDestination = startDestination
    ) {
        composable("start") {
            StartScreen(navController)
        }
        composable(
            route = "workout/{timerId}",
            arguments = listOf(
                navArgument("timerId") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) {
            WorkoutScreen(navController = navController)
        }
    }
}
