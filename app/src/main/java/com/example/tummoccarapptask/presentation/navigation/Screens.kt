package com.example.tummoccarapptask.presentation.navigation

sealed class Screens(val route: String) {
    object Home : Screens("HomeScreen")
    object AddCar : Screens("AddCarScreen")
}