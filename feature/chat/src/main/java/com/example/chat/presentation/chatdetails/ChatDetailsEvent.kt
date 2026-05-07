package com.example.chat.presentation.chatdetails

sealed interface ChatDetailsEvent {

    data object NavigateBack : ChatDetailsEvent

    data class NavigateToUserProfile(
        val userId: String
    ) : ChatDetailsEvent

    data class OpenAttachmentPicker(
        val chatId: String
    ) : ChatDetailsEvent

    data class ShowMessage(
        val message: String
    ) : ChatDetailsEvent
}