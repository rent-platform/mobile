package com.example.profile.presentation.profile

data class ProfileUiState(
    val fullName: String,
    val nickname: String?,
    val avatarUrl: String?,
    val bio: String?,
    val phone: String?,
    val email: String?,
    val role: String,
    val isPhoneVerified: Boolean,
    val isEmailVerified: Boolean,
    val isActive: Boolean,
    val registeredAt: String,
    val updatedAt: String?,

    val rating: String,
    val reviewsCount: Int,

    val activeItemsCount: Int,
    val draftItemsCount: Int,
    val moderationItemsCount: Int,
    val rejectedItemsCount: Int,
    val archivedItemsCount: Int,

    val rentedOutCount: Int,
    val rentedCount: Int,
    val rentalHistoryCount: Int
)