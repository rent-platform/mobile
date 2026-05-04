package com.example.marketplace.presentation.search.filters

sealed interface SearchFiltersAction {

    data object NavigateBack : SearchFiltersAction

    data class ApplyFilters(
        val filters: SearchFiltersUiState
    ) : SearchFiltersAction
}