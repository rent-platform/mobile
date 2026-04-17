package com.example.marketplace.presentation.catalog

sealed interface CatalogAction {
    data object NavigateToSearch : CatalogAction
    data object NavigateToFilters : CatalogAction
    data object NavigateToNotifications : CatalogAction
    data class NavigateToItemDetails(val itemId: String) : CatalogAction
}