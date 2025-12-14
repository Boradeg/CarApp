package com.example.tummoccarapptask.data.repository


import com.example.tummoccarapptask.data.local.CarDao
import com.example.tummoccarapptask.data.local.CarTable
import com.example.tummoccarapptask.presentation.model.Resource
import com.example.tummoccarapptask.presentation.model.VehicleItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarRepositoryImpl @Inject constructor(val dao : CarDao) : CarRepository {

    override fun getCars(): Flow<Resource<List<VehicleItem>>> {
        return dao.getCarData()
            .map<List<CarTable>, Resource<List<VehicleItem>>> { carList ->
                val vehicleItems = carList.map { car ->
                    VehicleItem(
                        model = car.model,
                        brand = car.brand,
                        number = car.number,
                        fuelType = car.fuelType,
                        year = car.year,
                        age = car.age
                    )
                }
                Resource.Success(vehicleItems)
            }
            .onStart { emit(Resource.Loading) }
            .catch { exception ->
                emit(Resource.Error("Exception: ${exception.message}"))
            }
    }
    override suspend fun addCar(car: CarTable): Resource<Unit> {
        return try {
            dao.insertCar(car)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Exception : "+e.message)
        }
    }
}

sealed class CarUiState {
    object Loading : CarUiState()
    data class Success(val cars: List<VehicleItem>) : CarUiState()
    data class Error(val message: String) : CarUiState()
}
enum class FilterType {
    BRAND,
    FUEL
}

data class FilterItem(
    val id: String,
    val title: String,
    val isSelected: Boolean = false
)

