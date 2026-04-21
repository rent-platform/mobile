package com.example.marketplace.presentation.itemdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ItemDetailsRoute(
    itemId: String,
    onBackClick: () -> Unit,
    onShareClick: (String) -> Unit,
    onRentClick: (String) -> Unit,
    viewModel: ItemDetailsViewModel = viewModel()
) {
    LaunchedEffect(itemId) {
        viewModel.loadItem(itemId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ItemDetailsScreen(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                ItemDetailsEvent.OnBackClick -> onBackClick()
                ItemDetailsEvent.OnShareClick -> onShareClick(uiState.title)
                ItemDetailsEvent.OnRentClick -> onRentClick(uiState.title)
                ItemDetailsEvent.OnFavoriteClick -> viewModel.onFavoriteClick()
                ItemDetailsEvent.OnRetryClick -> viewModel.onRetryClick(itemId)
            }
        }
    )
}