package com.example.core.demo.model

data class DemoDeal(
    val id: String,
    val itemId: String,
    val renterId: String,
    val ownerId: String,
    val startDate: String,
    val endDate: String,
    val pricingMode: DemoPricingMode,
    val pricePerDaySnapshot: Long?,
    val pricePerHourSnapshot: Long?,
    val totalPrice: Long,
    val depositAmount: Long,
    val status: DemoDealStatus,
    val rejectionReason: String?,
    val createdAt: String,
    val updatedAt: String
)

enum class DemoPricingMode {
    HOUR,
    DAY
}

enum class DemoDealStatus {
    PENDING,
    CONFIRMED,
    PAYMENT_PENDING,
    PAID,
    ACTIVE,
    COMPLETED,
    REJECTED,
    CANCELLED
}