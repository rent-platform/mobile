package com.example.marketplace.presentation.rentrequest

sealed interface RentRequestEvent {
    data object OnBackClick : RentRequestEvent
    data class OnDateClick(val date: String) : RentRequestEvent
    data object OnSubmitClick : RentRequestEvent
    data class OnStartDateInputChange(val value: String) : RentRequestEvent

    data class OnEndDateInputChange(val value: String) : RentRequestEvent
}