package com.example.chat.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ChatsUiState())
    val uiState: StateFlow<ChatsUiState> = _uiState.asStateFlow()

    private val _events = Channel<ChatsEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadChats()
    }

    fun onAction(action: ChatsAction) {
        when (action) {
            is ChatsAction.RoleClick -> onRoleClick(action.role)
            is ChatsAction.FilterClick -> onFilterClick(action.filter)
            is ChatsAction.ChatClick -> onChatClick(action.chatId)
            ChatsAction.RetryClick -> loadChats()
        }
    }

    private fun onRoleClick(role: ChatRole) {
        if (_uiState.value.selectedRole == role) return

        _uiState.update {
            it.copy(
                selectedRole = role,
                selectedFilter = ChatFilter.ALL
            )
        }

        loadChats()
    }

    private fun onFilterClick(filter: ChatFilter) {
        _uiState.update {
            it.copy(selectedFilter = filter)
        }
    }

    private fun onChatClick(chatId: String) {
        viewModelScope.launch {
            _events.send(
                ChatsEvent.NavigateToChatDetails(chatId = chatId)
            )
        }
    }

    private fun loadChats() {
        viewModelScope.launch {
            val role = _uiState.value.selectedRole

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            runCatching {
                // getChatsUseCase(role = role)

                delay(300)
                mockChats(role)
            }.onSuccess { chats ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        chats = chats,
                        errorMessage = null
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "Не удалось загрузить чаты"
                    )
                }
            }
        }
    }

    private fun mockChats(role: ChatRole): List<ChatItemUi> {
        return when (role) {
            ChatRole.RENTER -> renterChats
            ChatRole.OWNER -> ownerChats
        }
    }
}

private val renterChats = listOf(
    ChatItemUi(
        id = "renter_chat_1",
        imageUrl = null,
        authorNickname = "Александра с очень длинным никнеймом",
        authorAvatarUrl = null,
        announcementTitle = "Фотоаппарат Canon EOS 250D",
        lastMessage = "Здравствуйте! Можно арендовать на выходные?",
        lastMessageTime = "13:09",
        unreadCount = 2,
        orderStatus = ChatOrderStatus.REQUEST
    ),
    ChatItemUi(
        id = "renter_chat_2",
        imageUrl = null,
        authorNickname = "Дмитрий",
        authorAvatarUrl = null,
        announcementTitle = "Перфоратор Bosch",
        lastMessage = "Да, забрать можно сегодня после 18:00",
        lastMessageTime = "Вчера",
        unreadCount = 0,
        orderStatus = ChatOrderStatus.IN_RENT
    )
)

private val ownerChats = listOf(
    ChatItemUi(
        id = "owner_chat_1",
        imageUrl = null,
        authorNickname = "Мария",
        authorAvatarUrl = null,
        announcementTitle = "Палатка туристическая на 4 места",
        lastMessage = "Спасибо, всё вернула в хорошем состоянии",
        lastMessageTime = "21 апр.",
        unreadCount = 1,
        orderStatus = ChatOrderStatus.COMPLETED
    ),
    ChatItemUi(
        id = "owner_chat_2",
        imageUrl = null,
        authorNickname = "Иван",
        authorAvatarUrl = null,
        announcementTitle = "Шуруповёрт Makita",
        lastMessage = "Готов подтвердить аренду",
        lastMessageTime = "10:42",
        unreadCount = 3,
        orderStatus = ChatOrderStatus.CONFIRMED
    )
)