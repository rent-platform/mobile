package com.example.marketplace.presentation.itemdetails

import com.example.marketplace.presentation.catalog.CatalogItemUi

data class ItemDetailsUiState(
    val id: String = "",
    val categoryId: Long? = null,
    val isLoading: Boolean = false,
    val title: String = "",
    val description: String = "",
    val imageUrls: List<String> = emptyList(),
    val pricePerDay: Long? = null,
    val pricePerHour: Long? = null,
    val depositAmount: Long? = null,
    val city: String = "",
    val pickupLocation: String? = null,
    val ownerName: String = "",
    val ownerRating: Float? = null,
    val reviewsCount: Int = 0,
    val isFavorite: Boolean = false,
    val errorMessage: String? = null,
    val imageResIds: List<Int> = emptyList(),
    val createdAt: String = "",
    val availability: List<ItemAvailabilityDayUiState> = emptyList(),
    val similarItems: List<CatalogItemUi> = emptyList()
)

data class ItemAvailabilityDayUiState(
    val date: String,
    val isAvailable: Boolean
)