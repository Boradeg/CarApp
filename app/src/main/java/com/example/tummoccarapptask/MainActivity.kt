package com.example.tummoccarapptask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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






