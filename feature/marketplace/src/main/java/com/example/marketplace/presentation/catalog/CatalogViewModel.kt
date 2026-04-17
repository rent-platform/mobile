package com.example.marketplace.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketplace.data.mock.CatalogMockData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatalogViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogUiState(
        popularCategories = CatalogMockData.popularCategories,
        recommendedItems = CatalogMockData.recommendedItems
    ))
    val uiState = _uiState.asStateFlow()

    private val _actions = MutableSharedFlow<CatalogAction>()
    val actions = _actions.asSharedFlow()

    fun onEvent(event: CatalogEvent) {
        when (event) {
            CatalogEvent.SearchClicked -> {
                viewModelScope.launch {
                    _actions.emit(CatalogAction.NavigateToSearch)
                }
            }

            CatalogEvent.FilterClicked -> {
                viewModelScope.launch {
                    _actions.emit(CatalogAction.NavigateToFilters)
                }
            }

            CatalogEvent.NotificationsClicked -> {
                viewModelScope.launch {
                    _actions.emit(CatalogAction.NavigateToNotifications)
                }
            }
            is CatalogEvent.CategoryClicked -> {
                //фильтрация по категории
            }
            is CatalogEvent.ItemClicked -> {
                viewModelScope.launch {
                    _actions.emit(
                        CatalogAction.NavigateToItemDetails(event.itemId)
                    )
                }
            }
            is CatalogEvent.FavoriteClicked -> {
                _uiState.update { state ->
                    state.copy(
                        recommendedItems = state.recommendedItems.map { item ->
                            if (item.id == event.itemId) {
                                item.copy(isFavorite = !item.isFavorite)
                            } else {
                                item
                            }
                        }
                    )
                }
            }
        }
    }
}