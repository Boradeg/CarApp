package com.example.tummoccarapptask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tummoccarapptask.domain.usecases.GetFilteredCarsUseCase
import com.example.tummoccarapptask.presentation.model.AppliedFilter
import com.example.tummoccarapptask.presentation.model.FilterState
import com.example.tummoccarapptask.presentation.model.FilterType
import com.example.tummoccarapptask.presentation.model.Resource
import com.example.tummoccarapptask.presentation.model.UserData
import com.example.tummoccarapptask.presentation.model.VehicleItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CarViewModel @Inject constructor(
    private val getFilteredCarsUseCase: GetFilteredCarsUseCase
) : ViewModel() {


    private val _uiState = MutableStateFlow<Resource<List<VehicleItem>>>(Resource.Loading)
    val uiState: StateFlow<Resource<List<VehicleItem>>> = _uiState.asStateFlow()

    private val _filterState = MutableStateFlow(FilterState())
    val filterState = _filterState.asStateFlow()

    private val _appliedFilter = MutableStateFlow(AppliedFilter())


    init {
        fetchCars()
    }

    val userData = UserData(name = "Amin", totalVehicle = "1200", totalEv = "1700")


    private fun fetchCars() {
        getFilteredCarsUseCase(_appliedFilter.value)
            .onEach { result ->
                _uiState.value = result
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