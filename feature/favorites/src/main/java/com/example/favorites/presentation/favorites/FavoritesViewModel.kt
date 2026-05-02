package com.example.favorites.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    private val _event = Channel<FavoritesEvent>()
    val event = _event.receiveAsFlow()

    init {
        loadFavorites()
    }

    fun onAction(action: FavoritesAction) {
        when (action) {
            FavoritesAction.RetryClick -> {
                loadFavorites()
            }

            is FavoritesAction.ItemClick -> {
                openItemDetails(action.itemId)
            }

            is FavoritesAction.FavoriteClick -> {
                removeFromFavorites(action.itemId)
            }
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = FavoritesUiState.Loading

            try {
                val favorites = listOf(
                    FavoriteItemUi(
                        id = "1",
                        title = "Дрель Bosch Professional",
                        location = "Алматы",
                        pricePerDay = "2 000 ₽/день"
                    ),
                    FavoriteItemUi(
                        id = "2",
                        title = "Фотоаппарат Canon EOS",
                        location = "Астана",
                        pricePerDay = "5 000 ₽/день"
                    )
                )

                _uiState.value = if (favorites.isEmpty()) {
                    FavoritesUiState.Empty
                } else {
                    FavoritesUiState.Content(favorites)
                }
            } catch (exception: Exception) {
                _uiState.value = FavoritesUiState.Error(
                    message = exception.message ?: "Не удалось загрузить избранное"
                )
            }
        }
    }

    private fun removeFromFavorites(itemId: String) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState !is FavoritesUiState.Content) return@launch

            val previousItems = currentState.items

            val updatedItems = previousItems.filterNot { item ->
                item.id == itemId
            }

            _uiState.value = if (updatedItems.isEmpty()) {
                FavoritesUiState.Empty
            } else {
                FavoritesUiState.Content(updatedItems)
            }

            try {
                // TODO заменить на removeFavorite(itemId)

                _event.send(
                    FavoritesEvent.ShowMessage("Удалено из избранного")
                )
            } catch (exception: Exception) {
                _uiState.value = FavoritesUiState.Content(previousItems)

                _event.send(
                    FavoritesEvent.ShowMessage(
                        exception.message ?: "Не удалось удалить из избранного"
                    )
                )
            }
        }
    }

    private fun openItemDetails(itemId: String) {
        viewModelScope.launch {
            _event.send(
                FavoritesEvent.OpenItemDetails(itemId = itemId)
            )
        }
    }
}