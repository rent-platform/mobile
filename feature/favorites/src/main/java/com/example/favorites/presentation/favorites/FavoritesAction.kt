package com.example.favorites.presentation.favorites

sealed interface FavoritesAction {

    data object RetryClick : FavoritesAction

    data class ItemClick(
        val itemId: String
    ) : FavoritesAction

    data class FavoriteClick(
        val itemId: String
    ) : FavoritesAction
}