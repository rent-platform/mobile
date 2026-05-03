package com.example.deals.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DealsViewModel : ViewModel() {

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

        if (state.ownerDeals.isEmpty() && state.renterDeals.isEmpty() && !state.isLoading) {
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
                delay(500)
                // val ownerDeals = dealsRepository.getOwnerDeals()
                // val renterDeals = dealsRepository.getRenterDeals()
                MockDealsData.ownerDeals to MockDealsData.renterDeals
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

private object MockDealsData {

    val ownerDeals = listOf(
        DealListItemUi(
            id = "owner-deal-1",
            itemId = "item-1",
            title = "Дрель Bosch Professional",
            dateRange = "03 мая 2026 — 06 мая 2026",
            totalPrice = "1 500 ₽",
            depositAmount = "3 000 ₽",
            status = DealStatus.Pending,
            pricingMode = DealPricingMode.Day,
            imageResId = null
        ),
        DealListItemUi(
            id = "owner-deal-2",
            itemId = "item-2",
            title = "Проектор Xiaomi",
            dateRange = "10 мая 2026 — 12 мая 2026",
            totalPrice = "2 800 ₽",
            depositAmount = "5 000 ₽",
            status = DealStatus.Active,
            pricingMode = DealPricingMode.Day,
            imageResId = null
        ),
        DealListItemUi(
            id = "owner-deal-3",
            itemId = "item-3",
            title = "Фотоаппарат Canon EOS",
            dateRange = "15 мая 2026 — 18 мая 2026",
            totalPrice = "4 500 ₽",
            depositAmount = "7 000 ₽",
            status = DealStatus.Completed,
            pricingMode = DealPricingMode.Day,
            imageResId = null
        )
    )

    val renterDeals = listOf(
        DealListItemUi(
            id = "renter-deal-1",
            itemId = "item-4",
            title = "Шуруповёрт Makita",
            dateRange = "20 мая 2026 — 21 мая 2026",
            totalPrice = "900 ₽",
            depositAmount = "2 000 ₽",
            status = DealStatus.Confirmed,
            pricingMode = DealPricingMode.Day,
            imageResId = null
        ),
        DealListItemUi(
            id = "renter-deal-2",
            itemId = "item-5",
            title = "Камера GoPro Hero",
            dateRange = "24 мая 2026, 12:00 — 18:00",
            totalPrice = "1 200 ₽",
            depositAmount = null,
            status = DealStatus.Rejected,
            pricingMode = DealPricingMode.Hour,
            imageResId = null
        )
    )
}