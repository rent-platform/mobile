package com.example.chat.presentation.chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChatsRoute(
    onNavigateToChatDetails: (chatId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ChatsEvent.NavigateToChatDetails -> {
                    onNavigateToChatDetails(event.chatId)
                }
            }
        }
    }

    ChatsScreen(
        uiState = uiState,
        onRoleClick = { role ->
            viewModel.onAction(
                ChatsAction.RoleClick(role)
            )
        },
        onFilterClick = { filter ->
            viewModel.onAction(
                ChatsAction.FilterClick(filter)
            )
        },
        onChatClick = { chatId ->
            viewModel.onAction(
                ChatsAction.ChatClick(chatId)
            )
        },
        onRetryClick = {
            viewModel.onAction(
                ChatsAction.RetryClick
            )
        },
        modifier = modifier
    )
}