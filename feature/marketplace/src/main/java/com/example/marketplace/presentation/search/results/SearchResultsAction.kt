package com.example.marketplace.presentation.search.results

sealed interface SearchResultsAction {

    data object NavigateToSearchInput : SearchResultsAction

    data object NavigateToFilters : SearchResultsAction

    data class NavigateToItemDetails(
        val itemId: String
    ) : SearchResultsAction
}