package com.example.deals.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deals.data.FakeDealsRepositoryImpl
import com.example.deals.domain.DealsRepository
import com.example.deals.domain.model.DealRole
import com.example.deals.presentation.deals.DealsEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DealsViewModel(private val dealsRepository: DealsRepository = FakeDealsRepositoryImpl()) : ViewModel() {

    private val _uiState = MutableStateFlow(
        DealsUiState(
            selectedRole = DealRole.Owner,
            selectedFilter = DealFilter.All
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _actions = MutableSharedFlow<DealsAction>()
    val actions = _actions.asSharedFlow()

    fun onAuthorized() {
        val state = _uiState.value

        if (
            state.ownerDeals.isEmpty() &&
            state.renterDeals.isEmpty() &&
            !state.isLoading
        ) {
            loadDeals()
        }
    }

    fun onEvent(event: DealsEvent) {
        when (event) {
            is DealsEvent.RoleClicked -> {
                onRoleClicked(event.role)
            }

            is DealsEvent.FilterClicked -> {
                onFilterClicked(event.filter)
            }

            is DealsEvent.DealClicked -> {
                navigateToDealDetails(event.dealId)
            }

            DealsEvent.CreateListingClicked -> {
                navigateToCreateListing()
            }

            DealsEvent.RetryClicked -> {
                loadDeals()
            }
        }
    }

    private fun onRoleClicked(role: DealRole) {
        _uiState.update { state ->
            state.copy(
                selectedRole = role,
                selectedFilter = DealFilter.All
            )
        }
    }

    private fun onFilterClicked(filter: DealFilter) {
        _uiState.update { state ->
            state.copy(selectedFilter = filter)
        }
    }

    private fun navigateToDealDetails(dealId: String) {
        viewModelScope.launch {
            _actions.emit(
                DealsAction.NavigateToDealDetails(dealId)
            )
        }
    }

    private fun navigateToCreateListing() {
        viewModelScope.launch {
            _actions.emit(DealsAction.NavigateToCreateListing)
        }
    }

    private fun loadDeals() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            runCatching {
                val ownerDeals = dealsRepository.getOwnerDeals()
                val renterDeals = dealsRepository.getRenterDeals()

                ownerDeals to renterDeals
            }.onSuccess { (ownerDeals, renterDeals) ->
                _uiState.update { state ->
                    state.copy(
                        ownerDeals = ownerDeals,
                        renterDeals = renterDeals,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }.onFailure {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        errorMessage = "Не удалось загрузить сделки"
                    )
                }
            }
        }
    }
}