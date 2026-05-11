package com.example.deals.domain.model

data class DealItemDetails(
    val id: String,
    val title: String,
    val description: String?,
    val imageResId: Int?,
    val city: String?,
    val pickupLocation: String?,
    val pricePerDay: Long?,
    val pricePerHour: Long?,
    val depositAmount: Long
)