package com.example.marketplace.presentation.rentrequest

sealed interface RentRequestEvent {
    data object OnBackClick : RentRequestEvent
    data class OnDateClick(val date: String) : RentRequestEvent
    data object OnSubmitClick : RentRequestEvent
}