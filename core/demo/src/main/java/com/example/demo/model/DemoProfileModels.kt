package com.example.core.demo.model

data class DemoProfileStats(
    val activeItemsCount: Int,
    val draftItemsCount: Int,
    val moderationItemsCount: Int,
    val rejectedItemsCount: Int,
    val archivedItemsCount: Int,
    val rentedOutCount: Int,
    val rentedCount: Int,
    val rentalHistoryCount: Int
)