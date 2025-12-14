package com.example.tummoccarapptask.presentation.model

import com.example.tummoccarapptask.data.local.CarTable
import com.example.tummoccarapptask.data.repository.FilterItem
import com.example.tummoccarapptask.data.repository.FilterType

data class SelectionItem(
    val title: String,
    val icon: Int? = null
)
fun VehicleItem.toEntity(): CarTable {
    return CarTable(
        brand = brand,
        model = model,
        number = number,
        fuelType = fuelType,
        year = year,
        age = age
    )
}

sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
}
sealed class SubmitState {
    object Idle : SubmitState()
    object Loading : SubmitState()
    object Success : SubmitState()
    data class Error(val message: String) : SubmitState()
}
data class VehicleItem(
    val model: String,
    val brand: String,
    val number: String,
    val fuelType: String,
    val year: String,
    val age: String
)

data class UserData(val name: String, val totalVehicle:String, val totalEv:String)

data class CarFilter(
    val brands: Set<String> = emptySet(),
    val fuels: Set<String> = emptySet()
)
data class FilterItem(
    val id: String,
    val title: String,
    val isSelected: Boolean = false
)
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
data class AppliedFilter(
    val brands: Set<String> = emptySet(),
    val fuels: Set<String> = emptySet()
)



