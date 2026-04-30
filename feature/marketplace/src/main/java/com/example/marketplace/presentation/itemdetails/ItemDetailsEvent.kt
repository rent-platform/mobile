package com.example.marketplace.presentation.itemdetails

sealed interface ItemDetailsEvent {
    data object OnBackClick : ItemDetailsEvent
    data object OnShareClick : ItemDetailsEvent
    data object OnRentClick : ItemDetailsEvent
    data object OnFavoriteClick : ItemDetailsEvent
    data object OnRetryClick : ItemDetailsEvent
    data class OnSimilarItemClick(val itemId: String) : ItemDetailsEvent
    data class OnSimilarFavoriteClick(val itemId: String) : ItemDetailsEvent
    data object OnSimilarSeeMoreClick : ItemDetailsEvent
    data object OnOwnerClick : ItemDetailsEvent
    data object OnAskOwnerClick : ItemDetailsEvent
}