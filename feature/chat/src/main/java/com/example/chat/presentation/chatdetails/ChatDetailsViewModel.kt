package com.example.chat.presentation.chatdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.data.FakeChatRepositoryImpl
import com.example.chat.domain.ChatRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ChatDetailsViewModel(
    private val repository: ChatRepository = FakeChatRepositoryImpl()
) : ViewModel() {

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
                repository.getChatDetails(chatId)
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
}