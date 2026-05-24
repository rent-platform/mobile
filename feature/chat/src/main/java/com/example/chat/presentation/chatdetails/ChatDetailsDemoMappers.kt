package com.example.chat.presentation.chatdetails

import com.example.core.demo.DemoScenario
import com.example.core.demo.model.DemoChat
import com.example.core.demo.model.DemoChatMessage
import com.example.core.demo.model.DemoDealStatus
import com.example.core.demo.model.DemoPricingMode
import com.example.core.ui.toDemoDrawableRes

fun DemoChat.toChatDetailsUiState(
    currentUserId: String
): ChatDetailsUiState {
    val companionUserId = if (currentUserId == renterId) ownerId else renterId

    val companion = DemoScenario.findUserById(companionUserId)
    val item = DemoScenario.findItemById(itemId)
    val deal = dealId?.let { DemoScenario.findDealById(it) }

    return ChatDetailsUiState(
        isLoading = false,
        errorMessage = null,
        currentUserId = currentUserId,
        isMenuVisible = false,
        inputText = "",
        isSending = false,
        chat = ChatDetailsHeaderUi(
            chatId = id,
            companionUserId = companionUserId,
            companionNickname = companion?.fullName
                ?: companion?.nickname
                ?: "Пользователь",
            companionAvatarUrl = companion?.avatarUrl,
            companionOnlineStatus = ChatOnlineStatus.ONLINE
        ),
        item = ChatDetailsItemUi(
            itemId = itemId,
            imageResId = item?.imageKey?.toDemoDrawableRes(),
            title = item?.title ?: "Объявление",
            priceText = deal?.let {
                when (it.pricingMode) {
                    DemoPricingMode.DAY -> "${formatPrice(it.pricePerDaySnapshot)} ₽/сутки"
                    DemoPricingMode.HOUR -> "${formatPrice(it.pricePerHourSnapshot)} ₽/час"
                }
            } ?: item?.let {
                when {
                    it.pricePerDay != null -> "${formatPrice(it.pricePerDay)} ₽/сутки"
                    it.pricePerHour != null -> "${formatPrice(it.pricePerHour)} ₽/час"
                    else -> null
                }
            },
            dateRangeText = deal?.let {
                "${formatDemoDate(it.startDate)} — ${formatDemoDate(it.endDate)}"
            },
            depositText = deal?.let {
                "Залог ${formatPrice(it.depositAmount)} ₽"
            } ?: item?.let {
                "Залог ${formatPrice(it.depositAmount)} ₽"
            },
            status = deal?.status.toChatDetailsDealStatus()
        ),
        availableActions = deal?.status.toAvailableActions(
            isCurrentUserOwner = currentUserId == ownerId
        ),
        messages = messages.map { message ->
            message.toChatMessageUi(currentUserId)
        }
    )
}

private fun DemoChatMessage.toChatMessageUi(
    currentUserId: String
): ChatMessageUi {
    return when (this) {
        is DemoChatMessage.UserMessage -> {
            ChatMessageUi.UserMessage(
                id = id,
                senderId = senderId,
                text = text,
                time = time,
                isMine = senderId == currentUserId,
                isRead = isRead
            )
        }

        is DemoChatMessage.SystemMessage -> {
            ChatMessageUi.SystemMessage(
                id = id,
                text = text
            )
        }

        is DemoChatMessage.DateDivider -> {
            ChatMessageUi.DateDivider(
                id = id,
                title = title
            )
        }
    }
}

private fun DemoDealStatus?.toChatDetailsDealStatus(): ChatDetailsDealStatus? {
    return when (this) {
        DemoDealStatus.PENDING -> ChatDetailsDealStatus.REQUEST
        DemoDealStatus.CONFIRMED -> ChatDetailsDealStatus.CONFIRMED
        DemoDealStatus.PAYMENT_PENDING -> ChatDetailsDealStatus.PAYMENT_PENDING
        DemoDealStatus.PAID -> ChatDetailsDealStatus.PAID
        DemoDealStatus.ACTIVE -> ChatDetailsDealStatus.ACTIVE
        DemoDealStatus.COMPLETED -> ChatDetailsDealStatus.COMPLETED
        DemoDealStatus.REJECTED,
        DemoDealStatus.CANCELLED -> ChatDetailsDealStatus.CANCELLED
        null -> null
    }
}

private fun DemoDealStatus?.toAvailableActions(
    isCurrentUserOwner: Boolean
): List<ChatDealActionUi> {
    return when (this) {
        DemoDealStatus.PENDING -> {
            if (isCurrentUserOwner) {
                listOf(
                    ChatDealActionUi.CONFIRM_REQUEST,
                    ChatDealActionUi.REJECT_REQUEST
                )
            } else {
                listOf(ChatDealActionUi.CANCEL)
            }
        }

        DemoDealStatus.CONFIRMED -> {
            if (isCurrentUserOwner) {
                listOf(
                    ChatDealActionUi.CREATE_PAYMENT,
                    ChatDealActionUi.CANCEL
                )
            } else {
                listOf(ChatDealActionUi.CANCEL)
            }
        }

        DemoDealStatus.PAYMENT_PENDING -> {
            if (isCurrentUserOwner) {
                listOf(ChatDealActionUi.CANCEL)
            } else {
                listOf(
                    ChatDealActionUi.PAY,
                    ChatDealActionUi.CANCEL
                )
            }
        }
        DemoDealStatus.PAID -> {
            listOf(
                ChatDealActionUi.CONFIRM_START
            )
        }

        DemoDealStatus.ACTIVE -> {
            listOf(ChatDealActionUi.COMPLETE_RENT)
        }

        DemoDealStatus.COMPLETED -> {
            listOf(ChatDealActionUi.LEAVE_REVIEW)
        }

        DemoDealStatus.REJECTED,
        DemoDealStatus.CANCELLED,
        null -> emptyList()
    }
}

private fun formatPrice(value: Long?): String {
    return value
        ?.toString()
        ?.reversed()
        ?.chunked(3)
        ?.joinToString(" ")
        ?.reversed()
        ?: "0"
}

private fun formatDemoDate(value: String): String {
    return when {
        value.startsWith("2026-04-18") -> "18 апр."
        value.startsWith("2026-04-21") -> "21 апр."
        value.startsWith("2026-04-23") -> "23 апр."
        value.startsWith("2026-04-24") -> "24 апр."
        value.startsWith("2026-04-25") -> "25 апр."
        value.startsWith("2026-04-26") -> "26 апр."
        value.startsWith("2026-04-27") -> "27 апр."
        value.startsWith("2026-04-28") -> "28 апр."
        else -> value.take(10)
    }
}