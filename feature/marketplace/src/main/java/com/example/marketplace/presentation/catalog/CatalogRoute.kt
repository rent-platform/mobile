package com.example.marketplace.presentation.catalog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CatalogRoute(
    onNavigateToSearch: () -> Unit,
    onNavigateToFilters: () -> Unit,
    onNavigateToNotifications: () -> Unit
) {
    val viewModel: CatalogViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                CatalogAction.NavigateToSearch -> onNavigateToSearch()
                CatalogAction.NavigateToFilters -> onNavigateToFilters()
                CatalogAction.NavigateToNotifications -> onNavigateToNotifications()
            }
        }
    }

    CatalogScreen(
        uiState = uiState,
        onSearchClick = {
            viewModel.onEvent(CatalogEvent.SearchClicked)
        },
        onFilterClick = {
            viewModel.onEvent(CatalogEvent.FilterClicked)
        },
        onNotificationsClick = {
            viewModel.onEvent(CatalogEvent.NotificationsClicked)
        }
    )
}