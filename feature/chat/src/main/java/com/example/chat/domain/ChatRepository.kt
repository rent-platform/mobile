package com.example.chat.domain

import com.example.chat.presentation.chat.ChatItemUi
import com.example.chat.presentation.chat.ChatRole
import com.example.chat.presentation.chatdetails.ChatDetailsUiState

interface ChatRepository {

    suspend fun getChats(role: ChatRole): List<ChatItemUi>

    suspend fun getChatDetails(chatId: String): ChatDetailsUiState
}