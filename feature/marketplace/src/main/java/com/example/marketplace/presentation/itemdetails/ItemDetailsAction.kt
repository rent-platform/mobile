package com.example.marketplace.presentation.itemdetails

sealed interface ItemDetailsAction {
    data object NavigateBack : ItemDetailsAction
    data class ShareItem(val title: String) : ItemDetailsAction
    data class NavigateToRent(val title: String) : ItemDetailsAction
    data class NavigateToItemDetails(val itemId: String) : ItemDetailsAction
    data class NavigateToSimilarItems(val categoryId: Long?) : ItemDetailsAction
}