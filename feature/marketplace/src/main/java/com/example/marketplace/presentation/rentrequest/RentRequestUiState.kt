package com.example.marketplace.presentation.rentrequest

import com.example.marketplace.presentation.components.RentCalendarDayUi

data class RentRequestUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val itemId: String = "",
    val ownerId: String = "",

    val title: String = "",
    val imageResId: Int? = null,

    val city: String = "",
    val pickupLocation: String? = null,

    val pricePerDay: Long? = null,
    val pricePerHour: Long? = null,
    val depositAmount: Long = 0,

    val selectedStartDate: String? = null,
    val selectedEndDate: String? = null,

    val availability: List<RentCalendarDayUi> = emptyList()
)