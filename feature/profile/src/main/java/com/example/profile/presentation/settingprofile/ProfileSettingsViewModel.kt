package com.example.profile.presentation.profilesettings

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

class ProfileSettingsViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileSettingsUiState())
    val uiState: StateFlow<ProfileSettingsUiState> = _uiState.asStateFlow()

    private val _actions = MutableSharedFlow<ProfileSettingsAction>()
    val actions: SharedFlow<ProfileSettingsAction> = _actions.asSharedFlow()

    fun onEvent(event: ProfileSettingsEvent) {
        when (event) {
            ProfileSettingsEvent.ChangePasswordClicked -> {
                sendAction(ProfileSettingsAction.NavigateToChangePassword)
            }

            ProfileSettingsEvent.DeleteProfileClicked -> {
                _uiState.value = _uiState.value.copy(
                    isDeleteDialogVisible = true
                )
            }

            ProfileSettingsEvent.DismissDeleteDialogClicked -> {
                _uiState.value = _uiState.value.copy(
                    isDeleteDialogVisible = false
                )
            }

            ProfileSettingsEvent.ConfirmDeleteProfileClicked -> {
                deleteProfile()
            }
        }
    }

    private fun deleteProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isDeleting = true
            )

            runCatching {
                repository.deleteMyProfile()
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isDeleting = false,
                    isDeleteDialogVisible = false
                )

                _actions.emit(ProfileSettingsAction.ProfileDeleted)
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isDeleting = false
                )

                _actions.emit(
                    ProfileSettingsAction.ShowError(
                        error.message ?: "Не удалось удалить профиль"
                    )
                )
            }
        }
    }

    private fun sendAction(action: ProfileSettingsAction) {
        viewModelScope.launch {
            _actions.emit(action)
        }
    }
}