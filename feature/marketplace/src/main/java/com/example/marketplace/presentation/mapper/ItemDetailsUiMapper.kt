package com.example.marketplace.presentation.mapper

import com.example.core.ui.toDemoDrawableRes
import com.example.marketplace.domain.model.ItemAvailabilityDay
import com.example.marketplace.domain.model.ItemDetails
import com.example.marketplace.presentation.itemdetails.ItemAvailabilityDayUiState
import com.example.marketplace.presentation.itemdetails.ItemDetailsUiState

fun ItemDetails.toUi(): ItemDetailsUiState {
    return ItemDetailsUiState(
        isLoading = false,
        errorMessage = null,
        id = id,
        categoryId = categoryId,
        title = title,
        description = description,
        pricePerDay = pricePerDay,
        pricePerHour = pricePerHour,
        depositAmount = depositAmount,
        city = city,
        pickupLocation = pickupLocation,
        ownerId = ownerId,
        ownerName = ownerName,
        ownerRating = ownerRating,
        reviewsCount = reviewsCount,
        createdAt = createdAt,
        imageResIds = imageKeys.mapNotNull { imageKey ->
            imageKey.toDemoDrawableRes()
        },
        availability = availability.map { day ->
            day.toUi()
        },
        isFavorite = isFavorite,
        similarItems = similarItems.map { item ->
            item.toUi()
        }
    )
}

private fun ItemAvailabilityDay.toUi(): ItemAvailabilityDayUiState {
    return ItemAvailabilityDayUiState(
        date = date,
        isAvailable = isAvailable
    )
}