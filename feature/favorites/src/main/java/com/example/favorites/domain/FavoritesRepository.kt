package com.example.favorites.domain

import com.example.favorites.presentation.favorites.FavoriteItemUi

interface FavoritesRepository {

    suspend fun getFavorites(): List<FavoriteItemUi>

    suspend fun removeFromFavorites(itemId: String): List<FavoriteItemUi>
}