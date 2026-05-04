package com.example.marketplace.presentation.search.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketplace.data.search.SearchHistoryRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchInputViewModel(
    private val searchHistoryRepository: SearchHistoryRepository
) : ViewModel() {

    private val query = MutableStateFlow("")

    val uiState: StateFlow<SearchInputUiState> = combine(
        query,
        searchHistoryRepository.observeHistory()
    ) { currentQuery, history ->
        SearchInputUiState(
            query = currentQuery,
            history = history
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = SearchInputUiState()
    )

    private val _action = Channel<SearchInputAction>(Channel.BUFFERED)
    val action = _action.receiveAsFlow()

    fun onEvent(event: SearchInputEvent) {
        when (event) {
            is SearchInputEvent.QueryChanged -> {
                onQueryChanged(event.query)
            }

            is SearchInputEvent.SearchSubmitted -> {
                submitSearch(event.query)
            }

            is SearchInputEvent.HistoryClicked -> {
                submitSearch(event.query)
            }

            is SearchInputEvent.RemoveHistoryClicked -> {
                removeHistoryItem(event.query)
            }

            SearchInputEvent.ClearQueryClicked -> {
                clearQuery()
            }

            SearchInputEvent.ClearHistoryClicked -> {
                clearHistory()
            }

            SearchInputEvent.BackClicked -> {
                navigateBack()
            }
        }
    }

    private fun onQueryChanged(newQuery: String) {
        query.update { newQuery }
    }

    private fun submitSearch(rawQuery: String) {
        val trimmedQuery = rawQuery.trim()

        if (trimmedQuery.isBlank()) return

        viewModelScope.launch {
            searchHistoryRepository.addQuery(trimmedQuery)

            _action.send(
                SearchInputAction.NavigateToSearchResults(
                    query = trimmedQuery
                )
            )
        }
    }

    private fun removeHistoryItem(query: String) {
        viewModelScope.launch {
            searchHistoryRepository.removeQuery(query)
        }
    }

    private fun clearQuery() {
        query.update { "" }
    }

    private fun clearHistory() {
        viewModelScope.launch {
            searchHistoryRepository.clearHistory()
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _action.send(SearchInputAction.NavigateBack)
        }
    }
}