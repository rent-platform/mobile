package com.example.marketplace.presentation.catalog

sealed interface CatalogEvent {
    data object SearchClicked : CatalogEvent
    data object FilterClicked : CatalogEvent
    data object NotificationsClicked : CatalogEvent
}