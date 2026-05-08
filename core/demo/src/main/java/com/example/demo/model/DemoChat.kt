package com.example.core.demo.model

data class DemoChat(
    val id: String,
    val dealId: String?,
    val itemId: String,
    val renterId: String,
    val ownerId: String,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCountForCurrentUser: Int,
    val messages: List<DemoChatMessage>
)

sealed class DemoChatMessage {
    abstract val id: String

    data class UserMessage(
        override val id: String,
        val senderId: String,
        val text: String,
        val time: String,
        val isRead: Boolean
    ) : DemoChatMessage()

    data class SystemMessage(
        override val id: String,
        val text: String
    ) : DemoChatMessage()

    data class DateDivider(
        override val id: String,
        val title: String
    ) : DemoChatMessage()
}