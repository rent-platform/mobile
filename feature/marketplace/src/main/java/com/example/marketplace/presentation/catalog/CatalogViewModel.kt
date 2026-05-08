package com.example.marketplace.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketplace.presentation.mapper.toUi
import com.example.marketplace.domain.repository.CatalogRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatalogViewModel(private val catalogRepository: CatalogRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState = _uiState.asStateFlow()

    private val _actions = MutableSharedFlow<CatalogAction>()
    val actions = _actions.asSharedFlow()

    init {
        loadCatalog()
    }

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
                toggleFavorite(event.itemId)
            }
        }
    }
    private fun loadCatalog() {
        viewModelScope.launch {
            runCatching {
                catalogRepository.getCatalog()
            }.onSuccess { catalog ->
                _uiState.update { state ->
                    state.copy(
                        popularCategories = catalog.categories.map { category ->
                            category.toUi()
                        },
                        recommendedItems = catalog.recommendedItems.map { item ->
                            item.toUi()
                        }
                    )
                }
            }.onFailure { error ->
                error.printStackTrace()
                // Если в CatalogUiState нет errorMessage, пока можно оставить только printStackTrace().
            }
        }
    }

    private fun toggleFavorite(itemId: String) {
        viewModelScope.launch {
            runCatching {
                catalogRepository.toggleFavorite(itemId)
            }.onSuccess { isFavorite ->
                _uiState.update { state ->
                    state.copy(
                        recommendedItems = state.recommendedItems.map { item ->
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
}