package com.example.marketplace.presentation.search.filters

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchFiltersRoute(
    onNavigateBack: () -> Unit,
    onApplyFilters: (SearchFiltersResult) -> Unit,
    viewModel: SearchFiltersViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.action.collectLatest { action ->
            when (action) {
                SearchFiltersAction.NavigateBack -> {
                    onNavigateBack()
                }

                is SearchFiltersAction.ApplyFilters -> {
                    onApplyFilters(action.filters)
                }
            }
        }
    }

    SearchFiltersScreen(
        uiState = uiState,
        onBackClick = {
            viewModel.onEvent(SearchFiltersEvent.BackClicked)
        },
        onCategorySelected = { category ->
            viewModel.onEvent(
                SearchFiltersEvent.CategorySelected(category)
            )
        },
        onCitySelected = { city ->
            viewModel.onEvent(
                SearchFiltersEvent.CitySelected(city)
            )
        },
        onMinPricePerDayChange = { value ->
            viewModel.onEvent(
                SearchFiltersEvent.MinPricePerDayChanged(value)
            )
        },
        onMaxPricePerDayChange = { value ->
            viewModel.onEvent(
                SearchFiltersEvent.MaxPricePerDayChanged(value)
            )
        },
        onMinPricePerHourChange = { value ->
            viewModel.onEvent(
                SearchFiltersEvent.MinPricePerHourChanged(value)
            )
        },
        onMaxPricePerHourChange = { value ->
            viewModel.onEvent(
                SearchFiltersEvent.MaxPricePerHourChanged(value)
            )
        },
        onOnlyAvailableNowChange = { value ->
            viewModel.onEvent(
                SearchFiltersEvent.OnlyAvailableNowChanged(value)
            )
        },
        onResetClick = {
            viewModel.onEvent(SearchFiltersEvent.ResetClicked)
        },
        onApplyClick = {
            viewModel.onEvent(SearchFiltersEvent.ApplyClicked)
        }
    )
}