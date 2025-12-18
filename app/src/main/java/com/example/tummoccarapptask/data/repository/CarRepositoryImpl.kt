package com.example.tummoccarapptask.data.repository


import com.example.tummoccarapptask.data.local.CarDao
import com.example.tummoccarapptask.data.local.CarTable
import com.example.tummoccarapptask.data.model.toEntity
import com.example.tummoccarapptask.data.model.toVehicleItem
import com.example.tummoccarapptask.domain.repository.CarRepository
import com.example.tummoccarapptask.presentation.model.Resource
import com.example.tummoccarapptask.presentation.model.VehicleItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class CarRepositoryImpl @Inject constructor(val dao : CarDao) : CarRepository {

    override fun getCars(): Flow<Resource<List<VehicleItem>>> {
        return dao.getCarData()
            .map<List<CarTable>, Resource<List<VehicleItem>>> { carList ->
                val vehicleItems = carList.map { car ->
                    car.toVehicleItem()
                }
                Resource.Success(vehicleItems)
            }
            .onStart { emit(Resource.Loading) }
            .catch { exception ->
                emit(Resource.Error("Exception: ${exception.message}"))
            }
    }

    override suspend fun addCar(car: VehicleItem): Resource<Unit> {
        return try {
            dao.insertCar(car.toEntity())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Exception : "+e.message)
        }
    }
}


