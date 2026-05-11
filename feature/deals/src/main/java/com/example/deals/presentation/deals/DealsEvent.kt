package com.example.deals.presentation.deals

import com.example.deals.domain.model.DealRole
import com.example.deals.presentation.DealFilter

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