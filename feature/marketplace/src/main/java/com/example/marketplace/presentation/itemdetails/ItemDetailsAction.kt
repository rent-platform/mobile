package com.example.marketplace.presentation.itemdetails

sealed interface ItemDetailsAction {
    data object NavigateBack : ItemDetailsAction
    data class ShareItem(val title: String) : ItemDetailsAction
    data class NavigateToRent(val itemId: String) : ItemDetailsAction
    data class NavigateToItemDetails(val itemId: String) : ItemDetailsAction
    data class NavigateToSimilarItems(val categoryId: Long?) : ItemDetailsAction
    data class NavigateToOwnerProfile(val ownerId: String) : ItemDetailsAction
    data class NavigateToOwnerChat(val itemId: String, val ownerId: String
    ) : ItemDetailsAction
}