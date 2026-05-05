package com.example.marketplace.presentation.listing

sealed interface CreateListingAction {

    data object BackClick : CreateListingAction

    data object NextClick : CreateListingAction

    data object PublishClick : CreateListingAction

    data object AddPhotoClick : CreateListingAction

    data class RemovePhotoClick(val photoId: String
    ) : CreateListingAction

    data class TitleChanged(val value: String
    ) : CreateListingAction

    data class DescriptionChanged(val value: String
    ) : CreateListingAction

    data class CategorySelected(val category: ListingCategory
    ) : CreateListingAction

    data class PricePerDayChanged(val value: String
    ) : CreateListingAction

    data class PricePerHourChanged(val value: String
    ) : CreateListingAction

    data class DepositEnabledChanged(val value: Boolean
    ) : CreateListingAction

    data class DepositAmountChanged(val value: String
    ) : CreateListingAction

    data class CityChanged(val value: String
    ) : CreateListingAction

    data class PickupLocationChanged(val value: String
    ) : CreateListingAction
}