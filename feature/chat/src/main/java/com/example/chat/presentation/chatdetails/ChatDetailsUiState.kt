package com.example.chat.presentation.chatdetails

import androidx.compose.runtime.Immutable

@Immutable
data class ChatDetailsUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentUserId: String = "",
    val chat: ChatDetailsHeaderUi? = null,
    val item: ChatDetailsItemUi? = null,
    val availableActions: List<ChatDealActionUi> = emptyList(),
    val messages: List<ChatMessageUi> = emptyList(),
    val inputText: String = "",
    val isSending: Boolean = false,
    val isMenuVisible: Boolean = false
)

@Immutable
data class ChatDetailsHeaderUi(
    val chatId: String,
    val companionUserId: String,
    val companionNickname: String,
    val companionAvatarUrl: String?,
    val companionOnlineStatus: ChatOnlineStatus
)

enum class ChatOnlineStatus(
    val title: String
) {
    ONLINE("В сети"),
    OFFLINE("Не в сети"),
    TYPING("печатает...")
}

@Immutable
data class ChatDetailsItemUi(
    val itemId: String,
    val imageUrl: String?,
    val title: String,
    val priceText: String?,
    val dateRangeText: String?,
    val depositText: String?,
    val status: ChatDetailsDealStatus?
)

enum class ChatDetailsDealStatus(
    val title: String
) {
    REQUEST("Запрос"),
    CONFIRMED("Подтверждена"),
    ACTIVE("Активна"),
    COMPLETED("Завершена"),
    CANCELLED("Отменена")
}

enum class ChatDealActionUi(
    val title: String,
    val type: ChatDealActionType
) {
    TRANSFER_ITEM("Передать вещь", ChatDealActionType.PRIMARY),
    CANCEL("Отменить", ChatDealActionType.DANGER),
    COMPLETE_RENT("Завершить аренду", ChatDealActionType.PRIMARY),
    LEAVE_REVIEW("Оставить отзыв", ChatDealActionType.SECONDARY)
}

enum class ChatDealActionType {
    PRIMARY,
    SECONDARY,
    DANGER
}

@Immutable
sealed interface ChatMessageUi {
    val id: String

    @Immutable
    data class UserMessage(
        override val id: String,
        val senderId: String,
        val text: String,
        val time: String,
        val isMine: Boolean,
        val isRead: Boolean = false
    ) : ChatMessageUi

    @Immutable
    data class SystemMessage(
        override val id: String,
        val text: String
    ) : ChatMessageUi

    @Immutable
    data class DateDivider(
        override val id: String,
        val title: String
    ) : ChatMessageUi
}