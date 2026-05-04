package com.example.marketplace.presentation.search.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchFiltersViewModel(
    initialFilters: SearchFiltersUiState = SearchFiltersUiState()
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialFilters)
    val uiState: StateFlow<SearchFiltersUiState> = _uiState

    private val _action = Channel<SearchFiltersAction>(Channel.BUFFERED)
    val action = _action.receiveAsFlow()

    fun onEvent(event: SearchFiltersEvent) {
        when (event) {
            SearchFiltersEvent.BackClicked -> {
                navigateBack()
            }

            is SearchFiltersEvent.CategorySelected -> {
                selectCategory(event.category)
            }

            is SearchFiltersEvent.CitySelected -> {
                selectCity(event.city)
            }

            is SearchFiltersEvent.MinPricePerDayChanged -> {
                changeMinPricePerDay(event.value)
            }

            is SearchFiltersEvent.MaxPricePerDayChanged -> {
                changeMaxPricePerDay(event.value)
            }

            is SearchFiltersEvent.MinPricePerHourChanged -> {
                changeMinPricePerHour(event.value)
            }

            is SearchFiltersEvent.MaxPricePerHourChanged -> {
                changeMaxPricePerHour(event.value)
            }

            is SearchFiltersEvent.OnlyAvailableNowChanged -> {
                changeOnlyAvailableNow(event.value)
            }

            SearchFiltersEvent.ResetClicked -> {
                resetFilters()
            }

            SearchFiltersEvent.ApplyClicked -> {
                applyFilters()
            }
        }
    }

    private fun selectCategory(category: SearchFilterCategory?) {
        _uiState.update {
            it.copy(
                selectedCategory = category
            )
        }
    }

    private fun selectCity(city: SearchFilterCity?) {
        _uiState.update {
            it.copy(
                selectedCity = city
            )
        }
    }

    private fun changeMinPricePerDay(value: String) {
        _uiState.update {
            it.copy(
                minPricePerDay = value.onlyDigits()
            )
        }
    }

    private fun changeMaxPricePerDay(value: String) {
        _uiState.update {
            it.copy(
                maxPricePerDay = value.onlyDigits()
            )
        }
    }

    private fun changeMinPricePerHour(value: String) {
        _uiState.update {
            it.copy(
                minPricePerHour = value.onlyDigits()
            )
        }
    }

    private fun changeMaxPricePerHour(value: String) {
        _uiState.update {
            it.copy(
                maxPricePerHour = value.onlyDigits()
            )
        }
    }

    private fun changeOnlyAvailableNow(value: Boolean) {
        _uiState.update {
            it.copy(
                onlyAvailableNow = value
            )
        }
    }

    private fun resetFilters() {
        _uiState.value = SearchFiltersUiState()
    }

    private fun applyFilters() {
        viewModelScope.launch {
            _action.send(
                SearchFiltersAction.ApplyFilters(
                    filters = _uiState.value
                )
            )
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _action.send(SearchFiltersAction.NavigateBack)
        }
    }

    private fun String.onlyDigits(): String {
        return filter { char -> char.isDigit() }
    }
}