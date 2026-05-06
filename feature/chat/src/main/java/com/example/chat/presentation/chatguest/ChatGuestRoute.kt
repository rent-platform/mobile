package com.example.chat.presentation.chatguest

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.chat.presentation.ChatGuestScreen

@Composable
fun ChatGuestRoute(
    onNavigateToAuth: () -> Unit,
    modifier: Modifier = Modifier
) {
    ChatGuestScreen(
        onLoginClick = onNavigateToAuth,
        modifier = modifier
    )
}