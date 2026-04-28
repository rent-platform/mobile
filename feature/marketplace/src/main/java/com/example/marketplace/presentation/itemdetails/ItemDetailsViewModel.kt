package com.example.marketplace.presentation.itemdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketplace.data.mock.ItemDetailsMockData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ItemDetailsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ItemDetailsUiState(isLoading = false))
    val uiState: StateFlow<ItemDetailsUiState> = _uiState.asStateFlow()

    private val _actions = Channel<ItemDetailsAction>(Channel.BUFFERED)
    val actions = _actions.receiveAsFlow()

    fun loadItem(itemId: String) {
        _uiState.value = ItemDetailsMockData.getById(itemId)
            ?: ItemDetailsUiState(
                isLoading = false,
                errorMessage = "Товар не найден"
            )
    }

    fun onEvent(event: ItemDetailsEvent, itemId: String) {
        when (event) {
            ItemDetailsEvent.OnBackClick -> {
                sendAction(ItemDetailsAction.NavigateBack)
            }

            ItemDetailsEvent.OnShareClick -> {
                sendAction(ItemDetailsAction.ShareItem(_uiState.value.title))
            }

            ItemDetailsEvent.OnRentClick -> {
                sendAction(ItemDetailsAction.NavigateToRent(_uiState.value.title))
            }

            ItemDetailsEvent.OnFavoriteClick -> {
                _uiState.update { state ->
                    state.copy(isFavorite = !state.isFavorite)
                }
            }

            ItemDetailsEvent.OnRetryClick -> {
                loadItem(itemId)
            }

            is ItemDetailsEvent.OnSimilarItemClick -> {
                sendAction(ItemDetailsAction.NavigateToItemDetails(event.itemId))
            }

            is ItemDetailsEvent.OnSimilarFavoriteClick -> {
                _uiState.update { state ->
                    state.copy(
                        similarItems = state.similarItems.map { item ->
                            if (item.id == event.itemId) {
                                item.copy(isFavorite = !item.isFavorite)
                            } else {
                                item
                            }
                        }
                    )
                }
            }

            ItemDetailsEvent.OnSimilarSeeMoreClick -> {
                sendAction(
                    ItemDetailsAction.NavigateToSimilarItems(
                        categoryId = _uiState.value.categoryId
                    )
                )
            }
        }
    }

    private fun sendAction(action: ItemDetailsAction) {
        viewModelScope.launch {
            _actions.send(action)
        }
    }
}