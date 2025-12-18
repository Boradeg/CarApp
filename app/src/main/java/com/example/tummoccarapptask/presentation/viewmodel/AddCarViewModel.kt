package com.example.tummoccarapptask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tummoccarapptask.R
import com.example.tummoccarapptask.domain.usecases.AddCarUseCase
import com.example.tummoccarapptask.presentation.model.Resource
import com.example.tummoccarapptask.presentation.model.SelectionItem
import com.example.tummoccarapptask.presentation.model.SubmitState
import com.example.tummoccarapptask.presentation.model.VehicleFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddCarViewModel @Inject constructor(
    private val addCarUseCase: AddCarUseCase,
) : ViewModel() {

    private val _formState = MutableStateFlow(VehicleFormState())
    val formState = _formState.asStateFlow()

    private val _submitState = MutableSharedFlow<SubmitState>()
    val submitState = _submitState.asSharedFlow()


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
        _submitState.emit(SubmitState.Loading)

        when (val result = addCarUseCase(formState.value)) {
            is Resource.Success<*> -> {
                _submitState.emit(SubmitState.Success)
            }

            is Resource.Error -> {
                _submitState.emit(SubmitState.Error(result.message))
            }

            Resource.Loading -> {
                _submitState.emit(SubmitState.Loading)

            }
        }
    }


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


}