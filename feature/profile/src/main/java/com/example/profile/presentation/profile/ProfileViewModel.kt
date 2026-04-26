package com.example.profile.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(mockProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _actions = MutableSharedFlow<ProfileAction>()
    val actions: SharedFlow<ProfileAction> = _actions.asSharedFlow()

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
        }
    }

    private fun loadProfile() {
        // TODO: позже заменить на запрос в Repository:
        // val user = profileRepository.getMe()
        // val stats = profileRepository.getProfileStats()
        // _uiState.value = mapper(user, stats)

        _uiState.value = mockProfileUiState()
    }

    private fun sendAction(action: ProfileAction) {
        viewModelScope.launch {
            _actions.emit(action)
        }
    }
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