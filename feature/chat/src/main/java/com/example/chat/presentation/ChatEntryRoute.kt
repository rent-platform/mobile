package com.example.chat.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.chat.presentation.chat.ChatsRoute
import com.example.chat.presentation.chatguest.ChatGuestRoute

@Composable
fun ChatEntryRoute(
    isAuthorized: Boolean,
    onLoginClick: () -> Unit,
    onNavigateToChatDetails: (chatId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (isAuthorized) {
        ChatsRoute(
            modifier = modifier,
            onNavigateToChatDetails = onNavigateToChatDetails
        )
    } else {
        ChatGuestRoute(
            modifier = modifier,
            onNavigateToAuth = onLoginClick
        )
    }
}