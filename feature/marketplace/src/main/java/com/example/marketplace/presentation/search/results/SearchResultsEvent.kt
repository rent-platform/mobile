package com.example.marketplace.presentation.search.results

sealed interface SearchResultsEvent {

    data object SearchClicked : SearchResultsEvent

    data object FilterClicked : SearchResultsEvent

    data class RemoveFilterClicked(
        val filterId: String
    ) : SearchResultsEvent

    data class ItemClicked(
        val itemId: String
    ) : SearchResultsEvent

    data class FavoriteClicked(
        val itemId: String
    ) : SearchResultsEvent

    data object RetryClicked : SearchResultsEvent
}