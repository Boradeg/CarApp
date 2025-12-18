package com.example.tummoccarapptask.presentation.model

data class AppliedFilter(
    val brands: Set<String> = emptySet(),
    val fuels: Set<String> = emptySet()
)