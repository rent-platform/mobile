package com.example.marketplace.presentation.catalog

sealed interface CatalogEvent {
    data object SearchClicked : CatalogEvent
    data object FilterClicked : CatalogEvent
    data object NotificationsClicked : CatalogEvent
    data class CategoryClicked(val categoryId: Long) : CatalogEvent
    data class ItemClicked(val itemId: String) : CatalogEvent
    data class FavoriteClicked(val itemId: String) : CatalogEvent
}