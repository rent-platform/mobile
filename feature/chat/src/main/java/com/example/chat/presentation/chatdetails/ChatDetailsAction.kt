package com.example.chat.presentation.chatdetails

sealed interface ChatDetailsAction {

    data class LoadChat(
        val chatId: String
    ) : ChatDetailsAction

    data object BackClick : ChatDetailsAction

    data object MenuClick : ChatDetailsAction

    data object DismissMenu : ChatDetailsAction

    data object MoveToTrashClick : ChatDetailsAction

    data object RetryClick : ChatDetailsAction

    data object SendClick : ChatDetailsAction

    data class InputChanged(
        val value: String
    ) : ChatDetailsAction

    data class CompanionProfileClick(
        val userId: String
    ) : ChatDetailsAction

    data class DealActionClick(
        val action: ChatDealActionUi
    ) : ChatDetailsAction

    data class AttachClick(
        val chatId: String
    ) : ChatDetailsAction
}