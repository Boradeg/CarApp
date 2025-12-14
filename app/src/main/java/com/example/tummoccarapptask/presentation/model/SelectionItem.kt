package com.example.tummoccarapptask.presentation.model

import com.example.tummoccarapptask.data.local.CarTable
import com.example.tummoccarapptask.presentation.Screens.VehicleItem

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
data class UserData(val name: String, val totalVehicle:String, val totalEv:String)

