package com.example.chat.presentation.chatdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChatDetailsRoute(
    chatId: String,
    onNavigateBack: () -> Unit,
    onNavigateToProfile: (userId: String) -> Unit,
    onOpenAttachmentPicker: (chatId: String) -> Unit,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatDetailsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(chatId) {
        viewModel.onAction(
            ChatDetailsAction.LoadChat(chatId)
        )
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collectLatest { event ->
            when (event) {
                ChatDetailsEvent.NavigateBack -> {
                    onNavigateBack()
                }

                is ChatDetailsEvent.NavigateToUserProfile -> {
                    onNavigateToProfile(event.userId)
                }

                is ChatDetailsEvent.OpenAttachmentPicker -> {
                    onOpenAttachmentPicker(event.chatId)
                }

                is ChatDetailsEvent.ShowMessage -> {
                    onShowMessage(event.message)
                }
            }
        }
    }

    ChatDetailsScreen(
        uiState = uiState,
        onBackClick = {
            viewModel.onAction(ChatDetailsAction.BackClick)
        },
        onMenuClick = {
            viewModel.onAction(ChatDetailsAction.MenuClick)
        },
        onDismissMenu = {
            viewModel.onAction(ChatDetailsAction.DismissMenu)
        },
        onMoveToTrashClick = {
            viewModel.onAction(ChatDetailsAction.MoveToTrashClick)
        },
        onRetryClick = {
            viewModel.onAction(ChatDetailsAction.RetryClick)
        },
        onProfileClick = { userId ->
            viewModel.onAction(
                ChatDetailsAction.CompanionProfileClick(userId)
            )
        },
        onDealActionClick = { action ->
            viewModel.onAction(
                ChatDetailsAction.DealActionClick(action)
            )
        },
        onInputChanged = { value ->
            viewModel.onAction(
                ChatDetailsAction.InputChanged(value)
            )
        },
        onSendClick = {
            viewModel.onAction(ChatDetailsAction.SendClick)
        },
        onAttachClick = { currentChatId ->
            viewModel.onAction(
                ChatDetailsAction.AttachClick(currentChatId)
            )
        },
        modifier = modifier
    )
}