package com.example.tummoccarapptask.domain.usecases

import com.example.tummoccarapptask.domain.repository.CarRepository
import com.example.tummoccarapptask.presentation.model.AppliedFilter
import com.example.tummoccarapptask.presentation.model.Resource
import com.example.tummoccarapptask.presentation.model.VehicleItem

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFilteredCarsUseCase @Inject constructor(
    private val repository: CarRepository
) {
    operator fun invoke(filter: AppliedFilter): Flow<Resource<List<VehicleItem>>> {
        return repository.getCars().map { result ->
            if (result is Resource.Success) {
                val filteredList = result.data.filter { car ->
                    val brandMatch = filter.brands.isEmpty() || filter.brands.contains(car.brand)
                    val fuelMatch = filter.fuels.isEmpty() || filter.fuels.contains(car.fuelType)
                    brandMatch && fuelMatch
                }
                Resource.Success(filteredList)
            } else {
                result
            }
        }
    }
}

