package com.example.marketplace.presentation.itemdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun ItemDetailsRoute(
    itemId: String,
    onBackClick: () -> Unit,
    onShareClick: (String) -> Unit,
    onRentClick: (String) -> Unit,
    onSimilarItemClick: (String) -> Unit,
    onSimilarSeeMoreClick: (Long?) -> Unit,
    onOwnerClick: (String) -> Unit,
    onAskOwnerClick: (itemId: String, ownerId: String) -> Unit,
) {
    val viewModel: ItemDetailsViewModel = koinViewModel()
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
                    onRentClick(action.itemId)
                }

                is ItemDetailsAction.NavigateToItemDetails -> {
                    onSimilarItemClick(action.itemId)
                }

                is ItemDetailsAction.NavigateToSimilarItems -> {
                    onSimilarSeeMoreClick(action.categoryId)
                }
                is ItemDetailsAction.NavigateToOwnerProfile -> {
                    onOwnerClick(action.ownerId)
                }

                is ItemDetailsAction.NavigateToOwnerChat -> {
                    onAskOwnerClick(action.itemId, action.ownerId)
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