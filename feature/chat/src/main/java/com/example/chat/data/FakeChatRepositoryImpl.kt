package com.example.chat.data

import com.example.chat.domain.ChatRepository
import com.example.chat.presentation.chat.ChatItemUi
import com.example.chat.presentation.chat.ChatRole
import com.example.chat.presentation.chat.toChatItemUi
import com.example.chat.presentation.chatdetails.ChatDetailsUiState
import com.example.chat.presentation.chatdetails.toChatDetailsUiState
import com.example.core.demo.DemoIds
import com.example.core.demo.DemoScenario
import kotlinx.coroutines.delay

class FakeChatRepositoryImpl : ChatRepository {

    override suspend fun getChats(role: ChatRole): List<ChatItemUi> {
        delay(300)

        val chats = when (role) {
            ChatRole.RENTER -> DemoScenario.renterChats
            ChatRole.OWNER -> DemoScenario.ownerChats
        }

        return chats.map { chat ->
            chat.toChatItemUi(
                currentUserId = DemoIds.CURRENT_USER_ID
            )
        }
    }

    override suspend fun getChatDetails(chatId: String): ChatDetailsUiState {
        delay(300)

        val chat = DemoScenario.findChatById(chatId)
            ?: error("Чат не найден")

        return chat.toChatDetailsUiState(
            currentUserId = DemoIds.CURRENT_USER_ID
        )
    }
}