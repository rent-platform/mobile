package com.example.favorites.presentation.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FavoritesRoute(
    onItemClick: (String) -> Unit,
    viewModel: FavoritesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.event.collect { event ->
            when (event) {
                is FavoritesEvent.OpenItemDetails -> {
                    onItemClick(event.itemId)
                }

                is FavoritesEvent.ShowMessage -> {
                    // snackbar
                }
            }
        }
    }

    FavoritesScreen(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}