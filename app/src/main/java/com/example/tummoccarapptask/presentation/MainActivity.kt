package com.example.tummoccarapptask.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tummoccarapptask.presentation.Screens.AddCarScreen
import com.example.tummoccarapptask.presentation.Screens.HomeScreen
import com.example.tummoccarapptask.presentation.navigation.AppNavigation
import com.example.tummoccarapptask.presentation.theme.TummocCarAppTaskTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TummocCarAppTaskTheme {
                AppNavigation()
            }
        }
    }

}






