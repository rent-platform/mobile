package com.example.favorites.presentation.favorites

sealed interface FavoritesEvent {

    data class OpenItemDetails(
        val itemId: String
    ) : FavoritesEvent

    data class ShowMessage(
        val message: String
    ) : FavoritesEvent
}