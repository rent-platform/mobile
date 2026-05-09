package com.example.marketplace.presentation.rentrequest

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun RentRequestRoute(
    itemId: String,
    onBackClick: () -> Unit,
    onSubmitRentRequest: (
        itemId: String,
        ownerId: String,
        startDate: String,
        endDate: String
    ) -> Unit,
    viewModel: RentRequestViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(itemId) {
        viewModel.loadItem(itemId)
    }

    LaunchedEffect(viewModel) {
        viewModel.actions.collect { action ->
            when (action) {
                RentRequestAction.NavigateBack -> {
                    onBackClick()
                }

                is RentRequestAction.SubmitRentRequest -> {
                    onSubmitRentRequest(
                        action.itemId,
                        action.ownerId,
                        action.startDate,
                        action.endDate
                    )
                }
            }
        }
    }

    RentRequestScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}