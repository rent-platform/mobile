package com.example.deals.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.deals.presentation.deals.DealsEvent
import com.example.deals.presentation.deals.DealsScreen

@Composable
fun DealsRoute(
    modifier: Modifier = Modifier,
    onNavigateToDealDetails: (dealId: String) -> Unit,
    onNavigateToCreateListing: () -> Unit,
    viewModel: DealsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onAuthorized()
    }

    LaunchedEffect(viewModel) {
        viewModel.actions.collect { action ->
            when (action) {
                is DealsAction.NavigateToDealDetails -> {
                    onNavigateToDealDetails(action.dealId)
                }

                DealsAction.NavigateToCreateListing -> {
                    onNavigateToCreateListing()
                }
            }
        }
    }
    DealsScreen(
        modifier = modifier,
        uiState = uiState,
        onRoleClick = { role ->
            viewModel.onEvent(DealsEvent.RoleClicked(role))
        },
        onFilterClick = { filter ->
            viewModel.onEvent(DealsEvent.FilterClicked(filter))
        },
        onDealClick = { dealId ->
            viewModel.onEvent(DealsEvent.DealClicked(dealId))
        },
        onCreateListingClick = {
            viewModel.onEvent(DealsEvent.CreateListingClicked)
        },
        onRetryClick = {
            viewModel.onEvent(DealsEvent.RetryClicked)
        }
    )
}