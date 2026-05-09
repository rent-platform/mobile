package com.example.chat.presentation.chat

import com.example.core.demo.DemoScenario
import com.example.core.demo.model.DemoChat
import com.example.core.demo.model.DemoDealStatus
import com.example.core.ui.toDemoDrawableRes

fun DemoChat.toChatItemUi(
    currentUserId: String
): ChatItemUi {
    val companionUserId = if (currentUserId == renterId) ownerId else renterId

    val companion = DemoScenario.findUserById(companionUserId)
    val item = DemoScenario.findItemById(itemId)
    val deal = dealId?.let { DemoScenario.findDealById(it) }

    return ChatItemUi(
        id = id,
        imageResId = item?.imageKey?.toDemoDrawableRes(),
        authorNickname = companion?.nickname
            ?: companion?.fullName
            ?: "Пользователь",
        authorAvatarUrl = companion?.avatarUrl,
        announcementTitle = item?.title ?: "Объявление",
        lastMessage = lastMessage,
        lastMessageTime = lastMessageTime,
        unreadCount = unreadCountForCurrentUser,
        orderStatus = deal?.status.toChatOrderStatus()
    )
}

private fun DemoDealStatus?.toChatOrderStatus(): ChatOrderStatus {
    return when (this) {
        DemoDealStatus.PENDING -> ChatOrderStatus.REQUEST
        DemoDealStatus.CONFIRMED -> ChatOrderStatus.CONFIRMED
        DemoDealStatus.ACTIVE -> ChatOrderStatus.IN_RENT
        DemoDealStatus.COMPLETED -> ChatOrderStatus.COMPLETED
        DemoDealStatus.REJECTED,
        DemoDealStatus.CANCELLED,
        null -> ChatOrderStatus.CANCELLED
    }
}