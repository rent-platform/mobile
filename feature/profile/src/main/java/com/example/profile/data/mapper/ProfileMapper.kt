package com.example.profile.data.mapper

import com.example.profile.data.mock.MockProfileExtra
import com.example.profile.data.mock.MockProfileStats
import com.example.profile.data.remote.dto.UserResponseDto
import com.example.profile.presentation.profile.ProfileUiState

fun UserResponseDto.toProfileUiState(
    extra: MockProfileExtra = MockProfileExtra(),
    stats: MockProfileStats = MockProfileStats()
): ProfileUiState {
    return ProfileUiState(
        fullName = fullName,
        nickname = nickname,
        avatarUrl = avatarUrl,
        bio = bio,
        phone = phone,
        email = email,
        role = role,
        isActive = isActive,

        isPhoneVerified = extra.isPhoneVerified,
        isEmailVerified = extra.isEmailVerified,
        registeredAt = extra.registeredAt,
        updatedAt = extra.updatedAt,

        rating = stats.rating,
        reviewsCount = stats.reviewsCount,

        activeItemsCount = stats.activeItemsCount,
        draftItemsCount = stats.draftItemsCount,
        moderationItemsCount = stats.moderationItemsCount,
        rejectedItemsCount = stats.rejectedItemsCount,
        archivedItemsCount = stats.archivedItemsCount,

        rentedOutCount = stats.rentedOutCount,
        rentedCount = stats.rentedCount,
        rentalHistoryCount = stats.rentalHistoryCount
    )
}