package com.example.tummoccarapptask.domain.usecases

import com.example.tummoccarapptask.domain.repository.CarRepository
import com.example.tummoccarapptask.presentation.model.Resource
import com.example.tummoccarapptask.presentation.model.VehicleFormState
import com.example.tummoccarapptask.presentation.model.VehicleItem

import javax.inject.Inject

class AddCarUseCase @Inject constructor(
    private val repository: CarRepository
) {
    suspend operator fun invoke(state: VehicleFormState): Resource<Unit> {
        if (state.brand.isBlank() ||
            state.model.isBlank() ||
            state.fuelType.isBlank() ||
            state.vehicleNumber.isBlank() ||
            state.yearOfPurchase.isBlank() ||
            state.ownerName.isBlank()
        ) {
            return Resource.Error("Please fill all fields")
        }

        val car = VehicleItem(
            brand = state.brand,
            model = state.model,
            number = state.vehicleNumber,
            fuelType = state.fuelType,
            year = state.yearOfPurchase,
            age = state.yearOfPurchase
        )

        return repository.addCar(car)
    }
}