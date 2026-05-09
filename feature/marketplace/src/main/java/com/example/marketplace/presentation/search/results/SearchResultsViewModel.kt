package com.example.marketplace.presentation.search.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketplace.domain.model.CatalogSearchParams
import com.example.marketplace.domain.repository.CatalogRepository
import com.example.marketplace.presentation.mapper.toUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchResultsViewModel(
    private val initialQuery: String,
    private val catalogRepository: CatalogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SearchResultsUiState(
            query = initialQuery
        )
    )
    val uiState: StateFlow<SearchResultsUiState> = _uiState

    private val _action = Channel<SearchResultsAction>(Channel.BUFFERED)
    val action = _action.receiveAsFlow()

    init {
        loadResults()
    }

    fun onEvent(event: SearchResultsEvent) {
        when (event) {
            SearchResultsEvent.SearchClicked -> {
                sendAction(SearchResultsAction.NavigateToSearchInput)
            }

            SearchResultsEvent.FilterClicked -> {
                sendAction(SearchResultsAction.NavigateToFilters)
            }

            is SearchResultsEvent.RemoveFilterClicked -> {
                removeFilter(event.filterId)
            }

            is SearchResultsEvent.ItemClicked -> {
                sendAction(
                    SearchResultsAction.NavigateToItemDetails(
                        itemId = event.itemId
                    )
                )
            }

            is SearchResultsEvent.FavoriteClicked -> {
                toggleFavorite(event.itemId)
            }

            SearchResultsEvent.RetryClicked -> {
                loadResults()
            }
        }
    }

    private fun loadResults() {
        val query = _uiState.value.query.trim()

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            runCatching {
                catalogRepository.searchItems(
                    CatalogSearchParams(
                        query = query
                    )
                )
            }.onSuccess { items ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        errorMessage = null,
                        items = items.map { item ->
                            item.toUi()
                        }
                    )
                }
            }.onFailure { error ->
                error.printStackTrace()

                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        errorMessage = "Не удалось загрузить результаты поиска"
                    )
                }
            }
        }
    }

    private fun removeFilter(filterId: String) {
        _uiState.update { currentState ->
            currentState.copy(
                activeFilters = currentState.activeFilters.filterNot { filter ->
                    filter.id == filterId
                }
            )
        }

        loadResults()
    }

    private fun toggleFavorite(itemId: String) {
        viewModelScope.launch {
            runCatching {
                catalogRepository.toggleFavorite(itemId)
            }.onSuccess { isFavorite ->
                _uiState.update { currentState ->
                    currentState.copy(
                        items = currentState.items.map { item ->
                            if (item.id == itemId) {
                                item.copy(isFavorite = isFavorite)
                            } else {
                                item
                            }
                        }
                    )
                }
            }.onFailure { error ->
                error.printStackTrace()
            }
        }
    }

    private fun sendAction(action: SearchResultsAction) {
        viewModelScope.launch {
            _action.send(action)
        }
    }
}