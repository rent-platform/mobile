package com.example.chat.presentation.chat

import androidx.compose.runtime.Immutable

@Immutable
data class ChatsUiState(
    val selectedRole: ChatRole = ChatRole.OWNER,
    val selectedFilter: ChatFilter = ChatFilter.ALL,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val chats: List<ChatItemUi> = emptyList()
) {
    val description: String
        get() = when (selectedRole) {
            ChatRole.RENTER -> "Переписки по вещам, которые вы арендуете"
            ChatRole.OWNER -> "Переписки по вещам, которые вы сдаёте"
        }

    val visibleChats: List<ChatItemUi>
        get() = when (selectedFilter) {
            ChatFilter.ALL -> chats
            ChatFilter.UNREAD -> chats.filter { it.unreadCount > 0 }
        }
}

enum class ChatRole(
    val title: String
) {
    OWNER("Я сдаю"),
    RENTER("Я арендую")
}

enum class ChatFilter(
    val title: String
) {
    ALL("Все"),
    UNREAD("Непрочитанные")
}

@Immutable
data class ChatItemUi(
    val id: String,
    val imageResId: Int?,
    val authorNickname: String,
    val authorAvatarUrl: String?,
    val announcementTitle: String,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int,
    val orderStatus: ChatOrderStatus
)

enum class ChatOrderStatus(
    val title: String
) {
    REQUEST("Запрос"),
    CONFIRMED("Подтверждено"),
    IN_RENT("В аренде"),
    COMPLETED("Завершено"),
    CANCELLED("Отменено")
}