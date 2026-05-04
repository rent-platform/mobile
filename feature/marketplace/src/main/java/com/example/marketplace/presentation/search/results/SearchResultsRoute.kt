package com.example.marketplace.presentation.search.results

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SearchResultsRoute(
    query: String,
    onNavigateToSearchInput: () -> Unit,
    onNavigateToFilters: () -> Unit,
    onNavigateToItemDetails: (String) -> Unit,
    viewModel: SearchResultsViewModel = koinViewModel(
        parameters = {
            parametersOf(query)
        }
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.action.collectLatest { action ->
            when (action) {
                SearchResultsAction.NavigateToSearchInput -> {
                    onNavigateToSearchInput()
                }

                SearchResultsAction.NavigateToFilters -> {
                    onNavigateToFilters()
                }

                is SearchResultsAction.NavigateToItemDetails -> {
                    onNavigateToItemDetails(action.itemId)
                }
            }
        }
    }

    SearchResultsScreen(
        uiState = uiState,
        onSearchClick = {
            viewModel.onEvent(SearchResultsEvent.SearchClicked)
        },
        onFilterClick = {
            viewModel.onEvent(SearchResultsEvent.FilterClicked)
        },
        onRemoveFilterClick = { filterId ->
            viewModel.onEvent(
                SearchResultsEvent.RemoveFilterClicked(filterId)
            )
        },
        onItemClick = { itemId ->
            viewModel.onEvent(
                SearchResultsEvent.ItemClicked(itemId)
            )
        },
        onFavoriteClick = { itemId ->
            viewModel.onEvent(
                SearchResultsEvent.FavoriteClicked(itemId)
            )
        },
        onRetryClick = {
            viewModel.onEvent(SearchResultsEvent.RetryClicked)
        }
    )
}