package com.example.marketplace.presentation.search.results

import com.example.marketplace.presentation.catalog.CatalogItemUi

data class SearchResultsUiState(
    val query: String = "",
    val items: List<CatalogItemUi> = emptyList(),
    val activeFilters: List<SearchFilterUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val isEmpty: Boolean
        get() = !isLoading && errorMessage == null && items.isEmpty()
}

data class SearchFilterUi(
    val id: String,
    val title: String
)