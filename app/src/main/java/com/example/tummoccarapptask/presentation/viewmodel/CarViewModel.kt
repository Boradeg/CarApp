package com.example.tummoccarapptask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tummoccarapptask.R
import com.example.tummoccarapptask.data.repository.CarRepositoryImpl
import com.example.tummoccarapptask.data.repository.FilterType
import com.example.tummoccarapptask.presentation.model.AppliedFilter
import com.example.tummoccarapptask.presentation.model.FilterState
import com.example.tummoccarapptask.presentation.model.SelectionItem
import com.example.tummoccarapptask.presentation.model.VehicleFormState
import com.example.tummoccarapptask.presentation.model.Resource
import com.example.tummoccarapptask.presentation.model.SubmitState
import com.example.tummoccarapptask.presentation.model.UserData
import com.example.tummoccarapptask.presentation.model.VehicleItem
import com.example.tummoccarapptask.presentation.model.toEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
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

    private val _filterState = MutableStateFlow(FilterState())
    val filterState = _filterState.asStateFlow()

    private val _appliedFilter = MutableStateFlow(AppliedFilter())


    init {
        fetchCars()
    }

    val userData = UserData(name = "Amin", totalVehicle = "1200", totalEv = "1700")
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
            is Resource.Loading -> {
                _submitState.value = SubmitState.Loading
            }
        }
    }
    private fun fetchCars() {
        repository.getCars()
            .onEach { result ->
                if (result is Resource.Success) {
                    val filter = _appliedFilter.value

                    val filtered = result.data.filter { car ->
                        val brandMatch =
                            filter.brands.isEmpty() || filter.brands.contains(car.brand)

                        val fuelMatch =
                            filter.fuels.isEmpty() || filter.fuels.contains(car.fuelType)

                        brandMatch && fuelMatch
                    }

                    _uiState.value = Resource.Success(filtered)
                } else {
                    _uiState.value = result
                }
            }
            .onStart {
                delay(1000)
                _uiState.value = Resource.Loading
            }
            .launchIn(viewModelScope)
    }

    fun updateFilterType(type: FilterType) {
        _filterState.update { it.copy(selectedType = type) }
    }
    fun toggleFilterItem(type: FilterType, id: String) {
        _filterState.update { state ->
            when (type) {
                FilterType.BRAND -> state.copy(
                    brands = state.brands.map {
                        if (it.id == id) it.copy(isSelected = !it.isSelected) else it
                    }
                )
                FilterType.FUEL -> state.copy(
                    fuels = state.fuels.map {
                        if (it.id == id) it.copy(isSelected = !it.isSelected) else it
                    }
                )
            }
        }
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
    fun applyFilter() {
        val applied = AppliedFilter(
            brands = filterState.value.brands
                .filter { it.isSelected }
                .map { it.title }
                .toSet(),

            fuels = filterState.value.fuels
                .filter { it.isSelected }
                .map { it.title }
                .toSet()
        )

        _appliedFilter.value = applied
        fetchCars()
    }

    fun clearFilter() {
        _filterState.update {
            it.copy(
                brands = it.brands.map { b -> b.copy(isSelected = false) },
                fuels = it.fuels.map { f -> f.copy(isSelected = false) }
            )
        }
        _appliedFilter.value = AppliedFilter()
        fetchCars()
    }


}
