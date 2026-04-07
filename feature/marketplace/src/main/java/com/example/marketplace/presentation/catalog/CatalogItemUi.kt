package com.example.marketplace.presentation.catalog

data class CatalogItemUi(
    val id: String,
    val title: String,
    val imageUrl: String? = null,
    val isFavorite: Boolean = false
)