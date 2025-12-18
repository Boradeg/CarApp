package com.example.tummoccarapptask.domain.repository

import com.example.tummoccarapptask.presentation.model.Resource
import com.example.tummoccarapptask.presentation.model.VehicleItem

import kotlinx.coroutines.flow.Flow

interface CarRepository {
    fun getCars(): Flow<Resource<List<VehicleItem>>>
    suspend fun addCar(car: VehicleItem): Resource<Unit>
}