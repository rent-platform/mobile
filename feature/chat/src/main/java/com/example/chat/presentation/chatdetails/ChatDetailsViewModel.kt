package com.example.chat.presentation.chatdetails

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
import java.util.UUID

class ChatDetailsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ChatDetailsUiState())
    val uiState: StateFlow<ChatDetailsUiState> = _uiState.asStateFlow()

    private val _events = Channel<ChatDetailsEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var currentChatId: String? = null

    fun onAction(action: ChatDetailsAction) {
        when (action) {
            is ChatDetailsAction.LoadChat -> {
                loadChat(chatId = action.chatId)
            }

            ChatDetailsAction.BackClick -> {
                navigateBack()
            }

            ChatDetailsAction.MenuClick -> {
                showMenu()
            }

            ChatDetailsAction.DismissMenu -> {
                hideMenu()
            }

            ChatDetailsAction.MoveToTrashClick -> {
                moveChatToTrash()
            }

            ChatDetailsAction.RetryClick -> {
                currentChatId?.let { chatId ->
                    loadChat(chatId = chatId)
                }
            }

            is ChatDetailsAction.InputChanged -> {
                onInputChanged(action.value)
            }

            ChatDetailsAction.SendClick -> {
                sendMessage()
            }

            is ChatDetailsAction.CompanionProfileClick -> {
                navigateToProfile(action.userId)
            }

            is ChatDetailsAction.DealActionClick -> {
                onDealActionClick(action.action)
            }

            is ChatDetailsAction.AttachClick -> {
                openAttachmentPicker(action.chatId)
            }
        }
    }

    private fun loadChat(chatId: String) {
        if (currentChatId == chatId && _uiState.value.chat != null) return

        currentChatId = chatId

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    isMenuVisible = false
                )
            }

            runCatching {
                // getChatDetailsUseCase(chatId)
                delay(300)
                mockChatDetails(chatId)
            }.onSuccess { state ->
                _uiState.value = state
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "Не удалось загрузить чат"
                    )
                }
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(ChatDetailsEvent.NavigateBack)
        }
    }

    private fun showMenu() {
        _uiState.update {
            it.copy(isMenuVisible = true)
        }
    }

    private fun hideMenu() {
        _uiState.update {
            it.copy(isMenuVisible = false)
        }
    }

    private fun moveChatToTrash() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isMenuVisible = false)
            }
            // moveChatToTrashUseCase(chatId = currentChatId)

            _events.send(
                ChatDetailsEvent.ShowMessage("Чат перемещён в корзину")
            )

            _events.send(ChatDetailsEvent.NavigateBack)
        }
    }

    private fun onInputChanged(value: String) {
        _uiState.update {
            it.copy(inputText = value)
        }
    }

    private fun sendMessage() {
        val text = _uiState.value.inputText.trim()
        if (text.isBlank() || _uiState.value.isSending) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(isSending = true)
            }

            runCatching {
                // sendMessageUseCase(chatId = currentChatId, text = text)

                delay(200)

                ChatMessageUi.UserMessage(
                    id = UUID.randomUUID().toString(),
                    senderId = _uiState.value.currentUserId,
                    text = text,
                    time = "сейчас",
                    isMine = true,
                    isRead = false
                )
            }.onSuccess { message ->
                _uiState.update {
                    it.copy(
                        inputText = "",
                        isSending = false,
                        messages = it.messages + message
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(isSending = false)
                }

                _events.send(
                    ChatDetailsEvent.ShowMessage(
                        throwable.message ?: "Не удалось отправить сообщение"
                    )
                )
            }
        }
    }

    private fun navigateToProfile(userId: String) {
        viewModelScope.launch {
            _events.send(
                ChatDetailsEvent.NavigateToUserProfile(userId)
            )
        }
    }

    private fun onDealActionClick(action: ChatDealActionUi) {
        viewModelScope.launch {
            when (action) {
                ChatDealActionUi.TRANSFER_ITEM -> {
                    // startDealUseCase(dealId)

                    _uiState.update {
                        it.copy(
                            item = it.item?.copy(
                                status = ChatDetailsDealStatus.ACTIVE
                            ),
                            availableActions = listOf(
                                ChatDealActionUi.COMPLETE_RENT
                            ),
                            messages = it.messages + ChatMessageUi.SystemMessage(
                                id = UUID.randomUUID().toString(),
                                text = "Аренда началась"
                            )
                        )
                    }

                    _events.send(
                        ChatDetailsEvent.ShowMessage("Аренда началась")
                    )
                }

                ChatDealActionUi.CANCEL -> {
                    //cancelDealUseCase(dealId, reason)

                    _uiState.update {
                        it.copy(
                            item = it.item?.copy(
                                status = ChatDetailsDealStatus.CANCELLED
                            ),
                            availableActions = emptyList(),
                            messages = it.messages + ChatMessageUi.SystemMessage(
                                id = UUID.randomUUID().toString(),
                                text = "Заявка отменена"
                            )
                        )
                    }

                    _events.send(
                        ChatDetailsEvent.ShowMessage("Заявка отменена")
                    )
                }

                ChatDealActionUi.COMPLETE_RENT -> {
                    //completeDealUseCase(dealId)

                    _uiState.update {
                        it.copy(
                            item = it.item?.copy(
                                status = ChatDetailsDealStatus.COMPLETED
                            ),
                            availableActions = listOf(
                                ChatDealActionUi.LEAVE_REVIEW
                            ),
                            messages = it.messages + ChatMessageUi.SystemMessage(
                                id = UUID.randomUUID().toString(),
                                text = "Аренда завершена"
                            )
                        )
                    }

                    _events.send(
                        ChatDetailsEvent.ShowMessage("Аренда завершена")
                    )
                }

                ChatDealActionUi.LEAVE_REVIEW -> {
                    _events.send(
                        ChatDetailsEvent.ShowMessage("Экран отзыва будет добавлен позже")
                    )
                }
            }
        }
    }

    private fun openAttachmentPicker(chatId: String) {
        if (chatId.isBlank()) return

        viewModelScope.launch {
            _events.send(
                ChatDetailsEvent.OpenAttachmentPicker(chatId)
            )
        }
    }

    private fun mockChatDetails(chatId: String): ChatDetailsUiState {
        return ChatDetailsUiState(
            isLoading = false,
            errorMessage = null,
            currentUserId = "me",
            isMenuVisible = false,
            inputText = "",
            isSending = false,
            chat = ChatDetailsHeaderUi(
                chatId = chatId,
                companionUserId = "user_alexey",
                companionNickname = "Алексей Иванов",
                companionAvatarUrl = null,
                companionOnlineStatus = ChatOnlineStatus.TYPING
            ),
            item = ChatDetailsItemUi(
                itemId = "item_camera_1",
                imageUrl = null,
                title = "Canon EOS R5 + RF 24-70mm f/2.8L",
                priceText = "4 500 ₽/сутки",
                dateRangeText = "23 апр. — 25 апр.",
                depositText = "Залог 15 000 ₽",
                status = ChatDetailsDealStatus.CONFIRMED
            ),
            availableActions = listOf(
                ChatDealActionUi.TRANSFER_ITEM,
                ChatDealActionUi.CANCEL
            ),
            messages = listOf(
                ChatMessageUi.UserMessage(
                    id = "message_1",
                    senderId = "user_alexey",
                    text = "Супер! А залог какой?",
                    time = "16:15",
                    isMine = false
                ),
                ChatMessageUi.UserMessage(
                    id = "message_2",
                    senderId = "me",
                    text = "Залог 15 000 ₽, возвращается при сдаче в целости. Цена аренды 4 500 ₽ за 3 дня.",
                    time = "16:20",
                    isMine = true,
                    isRead = true
                ),
                ChatMessageUi.UserMessage(
                    id = "message_3",
                    senderId = "user_alexey",
                    text = "Отлично, оформляю заявку!",
                    time = "16:25",
                    isMine = false
                ),
                ChatMessageUi.SystemMessage(
                    id = "message_4",
                    text = "Алексей Иванов создал заявку на аренду"
                ),
                ChatMessageUi.SystemMessage(
                    id = "message_5",
                    text = "Вы подтвердили заявку"
                ),
                ChatMessageUi.UserMessage(
                    id = "message_6",
                    senderId = "me",
                    text = "Принял заявку ✅ Где вам удобно забрать?",
                    time = "17:20",
                    isMine = true,
                    isRead = true
                ),
                ChatMessageUi.DateDivider(
                    id = "date_1",
                    title = "22 апреля 2025 г."
                ),
                ChatMessageUi.UserMessage(
                    id = "message_7",
                    senderId = "user_alexey",
                    text = "Мне удобнее всего у метро Парк Культуры. Можно завтра в 14:00?",
                    time = "19:30",
                    isMine = false
                )
            )
        )
    }
}