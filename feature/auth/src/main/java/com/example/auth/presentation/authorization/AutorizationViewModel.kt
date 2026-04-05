package com.example.auth.presentation.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AutorizationViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(AuthorizationUiState())
    val uiState: StateFlow<AuthorizationUiState> = _uiState.asStateFlow()

    private val _actions = MutableSharedFlow<AuthorizationAction>()
    val actions: SharedFlow<AuthorizationAction> = _actions.asSharedFlow()

    fun onEvent(event: AuthorizationEvent) {
        when (event) {
            is AuthorizationEvent.PhoneChanged -> {
                val digits = event.value.filter { it.isDigit() }.take(10)
                _uiState.update {
                    it.copy(
                        phone = digits,
                        phoneError = null
                    )
                }
                updateLoginButtonState()
            }

            is AuthorizationEvent.PasswordChanged -> {
                _uiState.update {
                    it.copy(
                        password = event.value,
                        passwordError = null
                    )
                }
                updateLoginButtonState()
            }

            AuthorizationEvent.TogglePasswordVisibility -> {
                _uiState.update {
                    it.copy(isPasswordVisible = !it.isPasswordVisible)
                }
            }

            AuthorizationEvent.LoginClicked -> {
                submit()
            }

            AuthorizationEvent.RegisterClicked -> {
                viewModelScope.launch {
                    _actions.emit(AuthorizationAction.NavigateToRegistration)
                }
            }
        }
    }

    private fun updateLoginButtonState() {
        val state = _uiState.value

        val isPhoneValid = state.phone.length == 10
        val isPasswordFilled = state.password.isNotBlank()

        _uiState.update {
            it.copy(isLoginEnabled = isPhoneValid && isPasswordFilled)
        }
    }

    private fun submit() {
        val state = _uiState.value

        val phoneError =
            if (state.phone.length != 10) "Введите номер полностью" else null

        val passwordError =
            if (state.password.isBlank()) "Введите пароль" else null

        _uiState.update {
            it.copy(
                phoneError = phoneError,
                passwordError = passwordError
            )
        }

        val hasErrors = listOf(phoneError, passwordError).any { it != null }
        if (hasErrors) return

        login()
    }

    private fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // Временная заглушка.
                // Потом здесь будет repository.login(phone, password)
                val isSuccess = true

                if (isSuccess) {
                    _uiState.update { it.copy(isLoading = false) }
                    _actions.emit(AuthorizationAction.NavigateToCatalog)
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            passwordError = "Неверный номер телефона или пароль"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _actions.emit(
                    AuthorizationAction.ShowError("Ошибка сети. Попробуйте ещё раз")
                )
            }
        }
    }
}