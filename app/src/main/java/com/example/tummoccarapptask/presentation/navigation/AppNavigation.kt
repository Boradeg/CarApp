package com.example.tummoccarapptask.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tummoccarapptask.presentation.Screens.AddCarScreen
import com.example.tummoccarapptask.presentation.Screens.HomeRoute


@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route,
    ) {


        composable(route = Screens.Home.route) {
            HomeRoute(navController)
        }
        composable(route = Screens.AddCar.route) {
            AddCarScreen(navController)
        }
    }
}

