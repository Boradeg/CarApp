package com.example.tummoccarapptask.data.repository

import com.example.tummoccarapptask.data.local.CarTable
import com.example.tummoccarapptask.presentation.Screens.VehicleItem
import com.example.tummoccarapptask.presentation.model.Resource
import kotlinx.coroutines.flow.Flow

interface CarRepository {
    fun getCars(): Flow<Resource<List<VehicleItem>>>
    suspend fun addCar(car: CarTable): Resource<Unit>
}