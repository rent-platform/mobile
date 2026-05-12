package com.example.deals.presentation.dealdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.deals.presentation.details.DealDetailsScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DealDetailsRoute(
    dealId: String,
    onBackClick: () -> Unit,
    onNavigateToChat: (chatId: String) -> Unit,
    onNavigateToReview: (dealId: String) -> Unit,
    onShowMessage: (message: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DealDetailsViewModel = koinViewModel(
        parameters = {
            parametersOf(dealId)
        }
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.actions.collect { action ->
            when (action) {
                DealDetailsAction.NavigateBack -> {
                    onBackClick()
                }

                is DealDetailsAction.NavigateToChat -> {
                    onNavigateToChat(action.chatId)
                }

                is DealDetailsAction.NavigateToReview -> {
                    onNavigateToReview(action.dealId)
                }

                is DealDetailsAction.ShowMessage -> {
                    onShowMessage(action.message)
                }
            }
        }
    }

    DealDetailsScreen(
        modifier = modifier,
        uiState = uiState,
        onBackClick = {
            viewModel.onEvent(DealDetailsEvent.BackClicked)
        },
        onRetryClick = {
            viewModel.onEvent(DealDetailsEvent.RetryClicked)
        },
        onChatClick = { chatId ->
            viewModel.onEvent(
                DealDetailsEvent.ChatClicked(chatId)
            )
        },
        onDealActionClick = { action ->
            viewModel.onEvent(
                DealDetailsEvent.DealActionClicked(action)
            )
        }
    )
}