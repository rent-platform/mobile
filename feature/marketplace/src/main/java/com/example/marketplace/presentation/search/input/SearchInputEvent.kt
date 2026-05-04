package com.example.marketplace.presentation.search.input

sealed interface SearchInputEvent {

    data class QueryChanged(
        val query: String
    ) : SearchInputEvent

    data class SearchSubmitted(
        val query: String
    ) : SearchInputEvent

    data class HistoryClicked(
        val query: String
    ) : SearchInputEvent

    data class RemoveHistoryClicked(
        val query: String
    ) : SearchInputEvent

    data object ClearQueryClicked : SearchInputEvent

    data object ClearHistoryClicked : SearchInputEvent

    data object BackClicked : SearchInputEvent
}