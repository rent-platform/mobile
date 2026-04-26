package com.example.profile.data.mock

data class MockProfileStats(
    val rating: String = "4.8",
    val reviewsCount: Int = 24,

    val activeItemsCount: Int = 3,
    val draftItemsCount: Int = 2,
    val moderationItemsCount: Int = 1,
    val rejectedItemsCount: Int = 1,
    val archivedItemsCount: Int = 4,

    val rentedOutCount: Int = 12,
    val rentedCount: Int = 5,
    val rentalHistoryCount: Int = 8
)