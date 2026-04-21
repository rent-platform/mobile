package com.example.marketplace.presentation.itemdetails

import androidx.lifecycle.ViewModel
import com.example.marketplace.data.mock.ItemDetailsMockData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ItemDetailsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ItemDetailsUiState(isLoading = false))
    val uiState: StateFlow<ItemDetailsUiState> = _uiState.asStateFlow()

    fun loadItem(itemId: String) {
        _uiState.value = ItemDetailsMockData.getById(itemId)
            ?: ItemDetailsUiState(
                isLoading = false,
                errorMessage = "Товар не найден"
            )
    }

    fun onFavoriteClick() {
        _uiState.value = _uiState.value.copy(
            isFavorite = !_uiState.value.isFavorite
        )
    }

    fun onRetryClick(itemId: String) {
        loadItem(itemId)
    }
}