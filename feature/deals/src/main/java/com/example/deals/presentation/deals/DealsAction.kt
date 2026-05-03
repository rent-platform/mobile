package com.example.deals.presentation

sealed interface DealsAction {

    data class NavigateToDealDetails(
        val dealId: String
    ) : DealsAction

    data object NavigateToCreateListing : DealsAction
}