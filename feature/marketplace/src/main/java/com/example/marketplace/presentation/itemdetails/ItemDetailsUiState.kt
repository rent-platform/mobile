package com.example.marketplace.presentation.itemdetails

data class ItemDetailsUiState(
    val id: String = "",
    val isLoading: Boolean = false,
    val title: String = "",
    val description: String = "",
    val imageUrls: List<String> = emptyList(),
    val pricePerDay: Long? = null,
    val pricePerHour: Long? = null,
    val depositAmount: Long? = null,
    val location: String = "",
    val ownerName: String = "",
    val ownerRating: Float? = null,
    val reviewsCount: Int = 0,
    val isFavorite: Boolean = false,
    val errorMessage: String? = null,
    val imageResIds: List<Int> = emptyList()
)