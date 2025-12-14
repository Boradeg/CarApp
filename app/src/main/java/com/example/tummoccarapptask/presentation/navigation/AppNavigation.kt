package com.example.tummoccarapptask.presentation.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tummoccarapptask.presentation.Screens.AddCarScreen
import com.example.tummoccarapptask.presentation.Screens.HomeScreen
import com.example.tummoccarapptask.presentation.viewmodel.CarViewModel
import androidx.lifecycle.viewmodel.compose.viewModel as viewModel

@Composable
fun AppNavigation (){

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "Home",
    ) {



        composable(route = Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(route = Screen.AddCar.route) {
            AddCarScreen(navController)
        }
    }
}

sealed class Screen(val route: String){
    object Home: Screen("Home")
    object AddCar: Screen("add_car")
}