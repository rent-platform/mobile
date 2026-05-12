package com.example.deals.presentation.dealdetails

sealed interface DealDetailsAction {

    data object NavigateBack : DealDetailsAction

    data class NavigateToChat(
        val chatId: String
    ) : DealDetailsAction

    data class NavigateToReview(
        val dealId: String
    ) : DealDetailsAction

    data class ShowMessage(
        val message: String
    ) : DealDetailsAction
}