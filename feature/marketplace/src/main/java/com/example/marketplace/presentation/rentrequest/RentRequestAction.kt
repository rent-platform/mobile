package com.example.marketplace.presentation.rentrequest

sealed interface RentRequestAction {
    data object NavigateBack : RentRequestAction

    data class SubmitRentRequest(
        val itemId: String,
        val ownerId: String,
        val startDate: String,
        val endDate: String
    ) : RentRequestAction
}