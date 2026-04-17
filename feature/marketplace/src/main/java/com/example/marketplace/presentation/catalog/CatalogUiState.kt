package com.example.marketplace.presentation.catalog

data class CatalogUiState(
    val searchText: String = "",
    val popularCategories: List<CatalogCategoryUi> = emptyList(),
    val promo: CatalogPromoUi = CatalogPromoUi(),
    val recommendedItems: List<CatalogItemUi> = emptyList()
)

data class CatalogCategoryUi(
    val id: Long,
    val name: String
)

data class CatalogPromoUi(
    val title: String = "Арендай",
    val subtitle: String = "Безопасные сделки, удобный поиск и простой путь от выбора вещи до аренды"
)

data class CatalogItemUi(
    val id: String,
    val title: String,
    val pricePerDay: Long? = null,
    val location: String = "",
    val imageUrl: Int? = null,
    val isFavorite: Boolean = false
)