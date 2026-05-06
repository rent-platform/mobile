package com.example.chat.presentation.chat

sealed interface ChatsEvent {

    data class NavigateToChatDetails(
        val chatId: String
    ) : ChatsEvent
}