package com.example.deals.domain.model

data class DealDetails(
    val id: String,
    val item: DealItemDetails,

    val status: DealStatus,
    val role: DealRole,

    val renterId: String,
    val ownerId: String,

    val counterpartyName: String,
    val counterpartyAvatarResId: Int? = null,

    val startDate: String,
    val endDate: String,

    val totalPrice: Long,
    val depositAmount: Long,

    val rejectionReason: String? = null,

    val chatId: String? = null,

    val startConfirmedByMe: Boolean = false,
    val startConfirmedByOther: Boolean = false,
    val completeConfirmedByMe: Boolean = false,
    val completeConfirmedByOther: Boolean = false,

    val reviewLeftByMe: Boolean = false
)