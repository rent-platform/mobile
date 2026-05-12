package com.example.deals.presentation.dealdetails

import com.example.deals.presentation.details.DealDetailsActionUi

sealed interface DealDetailsEvent {

    data object BackClicked : DealDetailsEvent

    data object RetryClicked : DealDetailsEvent

    data class ChatClicked(
        val chatId: String
    ) : DealDetailsEvent

    data class DealActionClicked(
        val action: DealDetailsActionUi
    ) : DealDetailsEvent
}