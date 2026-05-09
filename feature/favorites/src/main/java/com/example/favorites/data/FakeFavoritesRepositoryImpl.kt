package com.example.favorites.data

import com.example.core.demo.DemoScenario
import com.example.core.demo.model.DemoItem
import com.example.core.ui.toDemoDrawableRes
import com.example.favorites.domain.FavoritesRepository
import com.example.favorites.presentation.favorites.FavoriteItemUi
import kotlinx.coroutines.delay

class FakeFavoritesRepositoryImpl : FavoritesRepository {

    private val favoriteIds = DemoScenario.favoriteItems
        .map { item -> item.id }
        .toMutableSet()

    override suspend fun getFavorites(): List<FavoriteItemUi> {
        delay(300)

        return DemoScenario.items
            .filter { item -> item.id in favoriteIds }
            .map { item -> item.toFavoriteItemUi() }
    }

    override suspend fun removeFromFavorites(itemId: String): List<FavoriteItemUi> {
        delay(150)

        favoriteIds.remove(itemId)

        return DemoScenario.items
            .filter { item -> item.id in favoriteIds }
            .map { item -> item.toFavoriteItemUi() }
    }

    private fun DemoItem.toFavoriteItemUi(): FavoriteItemUi {
        return FavoriteItemUi(
            id = id,
            title = title,
            location = city,
            pricePerDay = pricePerDay?.let { price ->
                "${formatPrice(price)} ₽/день"
            },
            isFavorite = true,
            imageResId = imageKey.toDemoDrawableRes()
        )
    }

    private fun formatPrice(value: Long): String {
        return value
            .toString()
            .reversed()
            .chunked(3)
            .joinToString(" ")
            .reversed()
    }
}