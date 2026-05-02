package com.example.favorites.presentation.favorites

data class FavoriteItemUi(
    val id: String,
    val title: String,
    val location: String,
    val pricePerDay: String?,
    val isFavorite: Boolean = true,
    val imageResId: Int? = null
)

sealed interface FavoritesUiState {

    data object Loading : FavoritesUiState

    data object Empty : FavoritesUiState

    data class Content(
        val items: List<FavoriteItemUi>
    ) : FavoritesUiState

    data class Error(
        val message: String
    ) : FavoritesUiState
}