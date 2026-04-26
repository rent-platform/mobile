package com.example.profile.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.profile.domain.ProfileRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(emptyProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _actions = MutableSharedFlow<ProfileAction>()
    val actions: SharedFlow<ProfileAction> = _actions.asSharedFlow()

    init {
        loadProfile()
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.EditProfileClicked -> {
                sendAction(ProfileAction.NavigateToEditProfile)
            }

            ProfileEvent.RatingClicked -> {
                sendAction(ProfileAction.NavigateToRating)
            }

            ProfileEvent.ActiveItemsClicked -> {
                sendAction(
                    ProfileAction.NavigateToMyItems(
                        status = ProfileItemsStatus.ACTIVE
                    )
                )
            }

            ProfileEvent.ModerationItemsClicked -> {
                sendAction(
                    ProfileAction.NavigateToMyItems(
                        status = ProfileItemsStatus.MODERATION
                    )
                )
            }

            ProfileEvent.RejectedItemsClicked -> {
                sendAction(
                    ProfileAction.NavigateToMyItems(
                        status = ProfileItemsStatus.REJECTED
                    )
                )
            }

            ProfileEvent.DraftItemsClicked -> {
                sendAction(
                    ProfileAction.NavigateToMyItems(
                        status = ProfileItemsStatus.DRAFT
                    )
                )
            }

            ProfileEvent.ArchivedItemsClicked -> {
                sendAction(
                    ProfileAction.NavigateToMyItems(
                        status = ProfileItemsStatus.ARCHIVED
                    )
                )
            }

            ProfileEvent.MyRentalsClicked -> {
                sendAction(ProfileAction.NavigateToMyRentals)
            }

            ProfileEvent.RentalHistoryClicked -> {
                sendAction(ProfileAction.NavigateToRentalHistory)
            }

            ProfileEvent.CreateItemClicked -> {
                sendAction(ProfileAction.NavigateToCreateItem)
            }

            ProfileEvent.RefreshProfile -> {
                loadProfile()
            }

            ProfileEvent.LogoutClicked -> {
                logout()
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            runCatching {
                repository.getMyProfile()
            }.onSuccess { profile ->
                _uiState.value = profile
            }.onFailure { error ->
                error.printStackTrace()

                _uiState.value = mockProfileUiState().copy(
                    fullName = "Ошибка загрузки профиля",
                    nickname = error.message ?: "Не удалось получить /api/users/me",
                    email = null,
                    phone = null
                )
            }
        }
    }

    private fun sendAction(action: ProfileAction) {
        viewModelScope.launch {
            _actions.emit(action)
        }
    }

    private fun logout() {
        viewModelScope.launch {
            repository.logout()
            _actions.emit(ProfileAction.Logout)
        }
    }
}

private fun emptyProfileUiState(): ProfileUiState {
    return ProfileUiState(
        fullName = "",
        nickname = null,
        avatarUrl = null,
        bio = null,
        phone = null,
        email = null,
        role = "user",
        isPhoneVerified = false,
        isEmailVerified = false,
        isActive = false,
        registeredAt = "",
        updatedAt = null,

        rating = "0.0",
        reviewsCount = 0,

        activeItemsCount = 0,
        draftItemsCount = 0,
        moderationItemsCount = 0,
        rejectedItemsCount = 0,
        archivedItemsCount = 0,

        rentedOutCount = 0,
        rentedCount = 0,
        rentalHistoryCount = 0
    )
}

private fun mockProfileUiState(): ProfileUiState {
    return ProfileUiState(
        fullName = "Сергей Иванов",
        nickname = "sergey_rent",
        avatarUrl = null,
        bio = "Сдаю технику",
        phone = "+7 900 123 45 67",
        email = "sergey@mail.ru",
        role = "user",
        isPhoneVerified = true,
        isEmailVerified = true,
        isActive = true,
        registeredAt = "12.04.2026",
        updatedAt = "25.04.2026",

        rating = "4.8",
        reviewsCount = 24,

        activeItemsCount = 3,
        draftItemsCount = 2,
        moderationItemsCount = 1,
        rejectedItemsCount = 1,
        archivedItemsCount = 4,

        rentedOutCount = 12,
        rentedCount = 5,
        rentalHistoryCount = 8
    )
}