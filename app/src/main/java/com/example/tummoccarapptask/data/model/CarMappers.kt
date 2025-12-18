package com.example.tummoccarapptask.data.model

import com.example.tummoccarapptask.data.local.CarTable
import com.example.tummoccarapptask.presentation.model.VehicleItem

fun CarTable.toVehicleItem() = VehicleItem(
    id = id,
    model = model,
    brand = brand,
    number = number,
    fuelType = fuelType,
    year = year,
    age = age
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
