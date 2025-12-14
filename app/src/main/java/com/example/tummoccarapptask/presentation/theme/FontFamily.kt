package com.example.tummoccarapptask.presentation.theme


import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.tummoccarapptask.R

val Satoshi = FontFamily(
    // Regular
    Font(R.font.satoshi_regular, FontWeight.Normal),

    // Medium
    Font(R.font.satoshi_medium, FontWeight.Medium),
    Font(R.font.satoshi_medium_italic, FontWeight.Medium, FontStyle.Italic),

    // Bold
    Font(R.font.satoshi_bold, FontWeight.Bold),
    Font(R.font.satoshi_bold_italic, FontWeight.Bold, FontStyle.Italic),

    // Light
    Font(R.font.satoshi_light, FontWeight.Light),
    Font(R.font.satoshi_light_italic, FontWeight.Light, FontStyle.Italic),

    // Black
    Font(R.font.satoshi_black, FontWeight.Black),
    Font(R.font.satoshi_black_italic, FontWeight.Black, FontStyle.Italic)
)

