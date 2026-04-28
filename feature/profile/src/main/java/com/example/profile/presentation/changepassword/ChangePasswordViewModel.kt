package com.example.profile.presentation.changepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.profile.domain.ProfileRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    private val _actions = MutableSharedFlow<ChangePasswordAction>()
    val actions: SharedFlow<ChangePasswordAction> = _actions.asSharedFlow()

    fun onEvent(event: ChangePasswordEvent) {
        when (event) {
            is ChangePasswordEvent.CurrentPasswordChanged -> {
                _uiState.update {
                    it.copy(
                        currentPassword = event.value,
                        currentPasswordError = null,
                        generalError = null
                    )
                }
            }

            is ChangePasswordEvent.NewPasswordChanged -> {
                _uiState.update {
                    it.copy(
                        newPassword = event.value,
                        newPasswordError = null,
                        generalError = null
                    )
                }
            }

            is ChangePasswordEvent.ConfirmNewPasswordChanged -> {
                _uiState.update {
                    it.copy(
                        confirmNewPassword = event.value,
                        confirmNewPasswordError = null,
                        generalError = null
                    )
                }
            }

            ChangePasswordEvent.SaveClicked -> {
                savePassword()
            }
        }
    }

    private fun savePassword() {
        val state = _uiState.value

        val currentPasswordError = when {
            state.currentPassword.isBlank() -> "Введите текущий пароль"
            else -> null
        }

        val newPasswordError = when {
            state.newPassword.isBlank() -> "Введите новый пароль"
            state.newPassword.length < 8 -> "Пароль должен быть не короче 8 символов"
            state.newPassword.length > 255 -> "Пароль слишком длинный"
            state.newPassword == state.currentPassword -> "Новый пароль должен отличаться от текущего"
            else -> null
        }

        val confirmPasswordError = when {
            state.confirmNewPassword.isBlank() -> "Повторите новый пароль"
            state.confirmNewPassword != state.newPassword -> "Пароли не совпадают"
            else -> null
        }

        if (
            currentPasswordError != null ||
            newPasswordError != null ||
            confirmPasswordError != null
        ) {
            _uiState.update {
                it.copy(
                    currentPasswordError = currentPasswordError,
                    newPasswordError = newPasswordError,
                    confirmNewPasswordError = confirmPasswordError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSaving = true,
                    generalError = null
                )
            }

            runCatching {
                repository.changePassword(
                    currentPassword = state.currentPassword,
                    newPassword = state.newPassword,
                    confirmNewPassword = state.confirmNewPassword
                )
            }.onSuccess {
                _uiState.update {
                    it.copy(isSaving = false)
                }

                _actions.emit(ChangePasswordAction.PasswordChanged)
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        generalError = error.message ?: "Не удалось сменить пароль"
                    )
                }
            }
        }
    }
}