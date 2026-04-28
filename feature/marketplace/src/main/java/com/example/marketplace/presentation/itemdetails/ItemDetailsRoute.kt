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
    onSimilarItemClick: (String) -> Unit,
    onSimilarSeeMoreClick: (Long?) -> Unit,
    viewModel: ItemDetailsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(itemId) {
        viewModel.loadItem(itemId)
    }

    LaunchedEffect(viewModel) {
        viewModel.actions.collect { action ->
            when (action) {
                ItemDetailsAction.NavigateBack -> onBackClick()

                is ItemDetailsAction.ShareItem -> {
                    onShareClick(action.title)
                }

                is ItemDetailsAction.NavigateToRent -> {
                    onRentClick(action.title)
                }

                is ItemDetailsAction.NavigateToItemDetails -> {
                    onSimilarItemClick(action.itemId)
                }

                is ItemDetailsAction.NavigateToSimilarItems -> {
                    onSimilarSeeMoreClick(action.categoryId)
                }
            }
        }
    }

    ItemDetailsScreen(
        uiState = uiState,
        onEvent = { event ->
            viewModel.onEvent(event, itemId)
        }
    )
}