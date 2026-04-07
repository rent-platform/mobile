package com.example.marketplace.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatalogViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogUiState())
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
        }
    }
}