package com.example.tummoccarapptask.presentation.model

data class FilterState(
    val selectedType: FilterType = FilterType.BRAND,
    val brands: List<FilterItem> = listOf(
        FilterItem("tata", "Tata"),
        FilterItem("honda", "Honda"),
        FilterItem("hero", "Hero"),
        FilterItem("bajaj", "Bajaj"),
        FilterItem("yamaha", "Yamaha"),
        FilterItem("other", "Other")
    ),
    val fuels: List<FilterItem> = listOf(
        FilterItem("petrol", "Petrol"),
        FilterItem("electric", "Electric"),
        FilterItem("diesel", "Diesel"),
        FilterItem("cng", "CNG")
    )
)