package com.example.core.demo.model

data class DemoItem(
    val id: String,
    val ownerId: String,
    val categoryId: Long,
    val title: String,
    val description: String,
    val pricePerDay: Long?,
    val pricePerHour: Long?,
    val depositAmount: Long,
    val city: String,
    val pickupLocation: String,
    val status: DemoItemStatus,
    val moderationComment: String?,
    val viewsCount: Int,
    val imageKey: String,
    val photoKeys: List<String>,
    val isFavorite: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val availability: List<DemoAvailabilityDay>
)

data class DemoAvailabilityDay(
    val date: String,
    val isAvailable: Boolean
)

enum class DemoItemStatus {
    DRAFT,
    MODERATION,
    ACTIVE,
    REJECTED,
    ARCHIVED
}