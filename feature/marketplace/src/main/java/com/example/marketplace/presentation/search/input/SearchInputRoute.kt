package com.example.marketplace.presentation.search.input

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchInputRoute(
    onNavigateBack: () -> Unit,
    onNavigateToSearchResults: (String) -> Unit,
    viewModel: SearchInputViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.action.collectLatest { action ->
            when (action) {
                SearchInputAction.NavigateBack -> {
                    onNavigateBack()
                }

                is SearchInputAction.NavigateToSearchResults -> {
                    onNavigateToSearchResults(action.query)
                }
            }
        }
    }

    SearchInputScreen(
        uiState = uiState,
        onQueryChange = { query ->
            viewModel.onEvent(
                SearchInputEvent.QueryChanged(query)
            )
        },
        onSearchSubmit = { query ->
            viewModel.onEvent(
                SearchInputEvent.SearchSubmitted(query)
            )
        },
        onHistoryClick = { query ->
            viewModel.onEvent(
                SearchInputEvent.HistoryClicked(query)
            )
        },
        onRemoveHistoryClick = { query ->
            viewModel.onEvent(
                SearchInputEvent.RemoveHistoryClicked(query)
            )
        },
        onClearQueryClick = {
            viewModel.onEvent(SearchInputEvent.ClearQueryClicked)
        },
        onBackClick = {
            viewModel.onEvent(SearchInputEvent.BackClicked)
        }
    )
}