package com.example.chat.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.data.FakeChatRepositoryImpl
import com.example.chat.domain.ChatRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatsViewModel(
    private val repository: ChatRepository = FakeChatRepositoryImpl()
) : ViewModel() {

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
                repository.getChats(role)
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
}