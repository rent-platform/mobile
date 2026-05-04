package com.example.marketplace.presentation.search.filters

data class SearchFiltersUiState(
    val categories: List<SearchFilterCategory> = SearchFilterCategory.entries,
    val selectedCategory: SearchFilterCategory? = null,

    val cities: List<SearchFilterCity> = SearchFilterCity.entries,
    val selectedCity: SearchFilterCity? = null,

    val minPricePerDay: String = "",
    val maxPricePerDay: String = "",

    val minPricePerHour: String = "",
    val maxPricePerHour: String = "",

    val onlyAvailableNow: Boolean = false
) {
    val hasHourlyPriceFilter: Boolean
        get() = minPricePerHour.isNotBlank() || maxPricePerHour.isNotBlank()

    val hasDailyPriceFilter: Boolean
        get() = minPricePerDay.isNotBlank() || maxPricePerDay.isNotBlank()

    val hasAppliedFilters: Boolean
        get() = selectedCategory != null ||
                selectedCity != null ||
                hasDailyPriceFilter ||
                hasHourlyPriceFilter ||
                onlyAvailableNow
}