package com.example.marketplace.domain.model

data class CatalogData(
    val categories: List<CatalogCategory>,
    val recommendedItems: List<CatalogItem>
)

data class CatalogCategory(
    val id: Long,
    val name: String
)

data class CatalogItem(
    val id: String,
    val title: String,
    val pricePerDay: Long?,
    val pricePerHour: Long?,
    val location: String,
    val imageKey: String?,
    val imageUrl: String?,
    val isFavorite: Boolean
)

data class ItemDetails(
    val id: String,
    val categoryId: Long,
    val title: String,
    val description: String,
    val pricePerDay: Long?,
    val pricePerHour: Long?,
    val depositAmount: Long,
    val city: String,
    val pickupLocation: String,
    val ownerId: String,
    val ownerName: String,
    val ownerRating: Float,
    val reviewsCount: Int,
    val createdAt: String,
    val imageKeys: List<String>,
    val imageUrls: List<String>,
    val availability: List<ItemAvailabilityDay>,
    val isFavorite: Boolean,
    val similarItems: List<CatalogItem>
)

data class ItemAvailabilityDay(
    val date: String,
    val isAvailable: Boolean
)

data class CatalogSearchParams(
    val query: String = "",
    val categoryId: Long? = null,
    val city: String? = null,
    val minPricePerDay: Long? = null,
    val maxPricePerDay: Long? = null,
    val minPricePerHour: Long? = null,
    val maxPricePerHour: Long? = null,
    val onlyAvailableNow: Boolean = false
)