package com.example.chat.presentation.chat

sealed interface ChatsAction {

    data class RoleClick(
        val role: ChatRole
    ) : ChatsAction

    data class FilterClick(
        val filter: ChatFilter
    ) : ChatsAction

    data class ChatClick(
        val chatId: String
    ) : ChatsAction

    data object RetryClick : ChatsAction
}