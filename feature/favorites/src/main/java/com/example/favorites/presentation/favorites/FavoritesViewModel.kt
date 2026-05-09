package com.example.favorites.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.favorites.data.FakeFavoritesRepositoryImpl
import com.example.favorites.domain.FavoritesRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: FavoritesRepository = FakeFavoritesRepositoryImpl()
) : ViewModel() {

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

            runCatching {
                repository.getFavorites()
            }.onSuccess { favorites ->
                _uiState.value = if (favorites.isEmpty()) {
                    FavoritesUiState.Empty
                } else {
                    FavoritesUiState.Content(favorites)
                }
            }.onFailure { exception ->
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

            runCatching {
                repository.removeFromFavorites(itemId)
            }.onSuccess { updatedItems ->
                _uiState.value = if (updatedItems.isEmpty()) {
                    FavoritesUiState.Empty
                } else {
                    FavoritesUiState.Content(updatedItems)
                }

                _event.send(
                    FavoritesEvent.ShowMessage("Удалено из избранного")
                )
            }.onFailure { exception ->
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