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
    private val emailRegex =
        Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    private val _uiState = MutableStateFlow(AuthorizationUiState())
    val uiState: StateFlow<AuthorizationUiState> = _uiState.asStateFlow()

    private val _actions = MutableSharedFlow<AuthorizationAction>()
    val actions: SharedFlow<AuthorizationAction> = _actions.asSharedFlow()

    fun onEvent(event: AuthorizationEvent) {
        when (event) {
            is AuthorizationEvent.LoginChanged -> {
                _uiState.update {
                    it.copy(
                        login = event.value,
                        loginError = null
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

        _uiState.update {
            it.copy(
                isLoginEnabled = state.login.isNotBlank() && state.password.isNotBlank()
            )
        }
    }

    private fun submit() {
        val state = _uiState.value

        val loginError = validateLogin(state.login)

        val passwordError =
            if (state.password.isBlank()) "Введите пароль" else null

        _uiState.update {
            it.copy(
                loginError = loginError,
                passwordError = passwordError
            )
        }

        val hasErrors = listOf(loginError, passwordError).any { it != null }
        if (hasErrors) return

        login()
    }

    private fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // Временная заглушка.
                // val result = repository.login(login, password, rememberMe = false)
                val isSuccess = true

                if (isSuccess) {
                    _uiState.update { it.copy(isLoading = false) }
                    _actions.emit(AuthorizationAction.NavigateToCatalog)
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            passwordError = "Неверный телефон/email или пароль"
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

    private fun validateLogin(value: String): String? {
        val input = value.trim()

        if (input.isBlank()) {
            return "Введите телефон или email"
        }

        return if (input.contains("@")) {
            validateEmail(input)
        } else {
            validatePhone(input)
        }
    }

    private fun validateEmail(email: String): String? {
        return if (!emailRegex.matches(email)) {
            "Введите корректный email"
        } else {
            null
        }
    }

    private fun validatePhone(phone: String): String? {
        val digits = phone.filter { it.isDigit() }

        return when {
            digits.length == 10 -> null
            digits.length == 11 && (digits.startsWith("7") || digits.startsWith("8")) -> null
            else -> "Введите корректный номер телефона"
        }
    }
}