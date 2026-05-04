package com.example.marketplace.presentation.search.input

data class SearchInputUiState(
    val query: String = "",
    val history: List<String> = emptyList()
)