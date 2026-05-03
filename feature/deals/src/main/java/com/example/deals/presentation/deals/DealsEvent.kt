package com.example.deals.presentation

sealed interface DealsEvent {

    data class RoleClicked(
        val role: DealRole
    ) : DealsEvent

    data class FilterClicked(
        val filter: DealFilter
    ) : DealsEvent

    data class DealClicked(
        val dealId: String
    ) : DealsEvent

    data object CreateListingClicked : DealsEvent

    data object RetryClicked : DealsEvent
}