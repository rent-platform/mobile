package com.example.marketplace.presentation.listing

sealed interface CreateListingEvent {

    data object NavigateBack : CreateListingEvent

    data class ShowMessage(
        val message: String
    ) : CreateListingEvent

    data class ListingPublished(
        val itemId: String
    ) : CreateListingEvent
}