package com.example.tummoccarapptask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tummoccarapptask.R
import com.example.tummoccarapptask.data.local.CarTable
import com.example.tummoccarapptask.data.repository.CarRepositoryImpl
import com.example.tummoccarapptask.presentation.model.SelectionItem
import com.example.tummoccarapptask.presentation.model.VehicleFormState
import com.example.tummoccarapptask.presentation.Screens.VehicleItem
import com.example.tummoccarapptask.presentation.model.Resource
import com.example.tummoccarapptask.presentation.model.SubmitState
import com.example.tummoccarapptask.presentation.model.UserData
import com.example.tummoccarapptask.presentation.model.toEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarViewModel @Inject constructor(
    private val repository: CarRepositoryImpl
) : ViewModel() {

    private val _formState = MutableStateFlow(VehicleFormState())
    val formState = _formState.asStateFlow()

    private val _submitState = MutableStateFlow<SubmitState>(SubmitState.Idle)
    val submitState = _submitState.asStateFlow()


    private val _uiState =
        MutableStateFlow<Resource<List<VehicleItem>>>(Resource.Loading)
    val uiState: StateFlow<Resource<List<VehicleItem>>> = _uiState.asStateFlow()




    init {
        fetchCars()
    }


    val UserData = UserData(name = "Amin", totalVehicle = "1200", totalEv = "1700")


    // -------- Lists --------
    val brandList = listOf(
        SelectionItem("Tata", R.drawable.img_tata),
        SelectionItem("Honda", R.drawable.img_honda),
        SelectionItem("Hero", R.drawable.img_hero),
        SelectionItem("Bajaj", R.drawable.img_bajaj),
        SelectionItem("Yamaha", R.drawable.img_yamaha),
        SelectionItem("Other")
    )

    val fuelList = listOf(
        SelectionItem("Petrol"),
        SelectionItem("Electric"),
        SelectionItem("Diesel"),
        SelectionItem("CNG")
    )

    val modelList = listOf(
        SelectionItem("Activa 4G"),
        SelectionItem("Activa 5G"),
        SelectionItem("Activa 6G"),
        SelectionItem("Activa 125"),
        SelectionItem("Activa 125 BS6"),
        SelectionItem("Activa H-Smart")
    )

    fun addCar() = viewModelScope.launch {
        val state = formState.value

        // -------- Validation --------
        if (
            state.brand.isBlank() ||
            state.model.isBlank() ||
            state.fuelType.isBlank() ||
            state.vehicleNumber.isBlank() ||
            state.yearOfPurchase.isBlank() ||
            state.ownerName.isBlank()
        ) {
            _submitState.value = SubmitState.Error("Please fill all fields")
            return@launch
        }

        _submitState.value = SubmitState.Loading

        val car = VehicleItem(
            brand = state.brand,
            model = state.model,
            number = state.vehicleNumber,
            fuelType = state.fuelType,
            year = state.yearOfPurchase,
            age = state.yearOfPurchase
        )

        when (repository.addCar(car.toEntity())) {
            is Resource.Success -> {
                _submitState.value = SubmitState.Success
            }
            is Resource.Error -> {
                _submitState.value =
                    SubmitState.Error("Something went wrong. Try again")
            }
            else -> Unit
        }
    }
    fun fetchCars() {
        repository.getCars()
            .onEach { _uiState.value = it }
            .launchIn(viewModelScope)
    }
    // -------- Field updates --------
    fun updateBrand(value: String) =
        _formState.update { it.copy(brand = value) }

    fun updateModel(value: String) =
        _formState.update { it.copy(model = value) }

    fun updateFuelType(value: String) =
        _formState.update { it.copy(fuelType = value) }

    fun updateVehicleNumber(value: String) =
        _formState.update { it.copy(vehicleNumber = value) }

    fun updateYearOfPurchase(value: String) =
        _formState.update { it.copy(yearOfPurchase = value) }

    fun updateOwnerName(value: String) =
        _formState.update { it.copy(ownerName = value) }

    fun resetSubmitState() {
        _submitState.value = SubmitState.Idle
    }
}
