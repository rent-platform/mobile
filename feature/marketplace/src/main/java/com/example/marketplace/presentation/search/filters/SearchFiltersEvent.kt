package com.example.marketplace.presentation.search.filters

sealed interface SearchFiltersEvent {

    data object BackClicked : SearchFiltersEvent

    data class CategorySelected(
        val category: SearchFilterCategory?
    ) : SearchFiltersEvent

    data class CitySelected(
        val city: SearchFilterCity?
    ) : SearchFiltersEvent

    data class MinPricePerDayChanged(
        val value: String
    ) : SearchFiltersEvent

    data class MaxPricePerDayChanged(
        val value: String
    ) : SearchFiltersEvent

    data class MinPricePerHourChanged(
        val value: String
    ) : SearchFiltersEvent

    data class MaxPricePerHourChanged(
        val value: String
    ) : SearchFiltersEvent

    data class OnlyAvailableNowChanged(
        val value: Boolean
    ) : SearchFiltersEvent

    data object ResetClicked : SearchFiltersEvent

    data object ApplyClicked : SearchFiltersEvent
}