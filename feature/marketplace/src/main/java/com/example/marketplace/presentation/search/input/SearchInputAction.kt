package com.example.marketplace.presentation.search.input

sealed interface SearchInputAction {

    data class NavigateToSearchResults(
        val query: String
    ) : SearchInputAction

    data object NavigateBack : SearchInputAction
}