package com.example.tummoccarapptask.presentation.model

data class FilterItem(
    val id: String,
    val title: String,
    val isSelected: Boolean = false
)