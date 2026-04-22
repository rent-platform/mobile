package com.example.auth.presentation.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    private val _actions = MutableSharedFlow<RegistrationAction>()
    val actions = _actions.asSharedFlow()

    private val nickNameRegex =
        Regex("^[а-яёА-ЯЁa-zA-Z]+(?:[ -][а-яёА-ЯЁa-zA-Z]+)*$")

    private val passwordRegex = Regex("^[A-Za-z\\d@#$%^&+=!]{6,20}$")

    fun onEvent(event: RegistrationEvent) {
        when (event) {
            is RegistrationEvent.PhoneChanged -> {
                val digits = event.value.filter { it.isDigit() }.take(10)
                _uiState.update {
                    it.copy(
                        phone = digits,
                        phoneError = null
                    )
                }
                updateButtonState()
            }

            is RegistrationEvent.FullNameChanged -> {
                _uiState.update {
                    it.copy(
                        nickname = event.value,
                        fullNameError = null
                    )
                }
                updateButtonState()
            }

            is RegistrationEvent.PasswordChanged -> {
                _uiState.update {
                    it.copy(
                        password = event.value,
                        passwordError = null
                    )
                }
                updateButtonState()
            }

            is RegistrationEvent.ConfirmPasswordChanged -> {
                _uiState.update {
                    it.copy(
                        confirmPassword = event.value,
                        confirmPasswordError = null
                    )
                }
                updateButtonState()
            }

            RegistrationEvent.TogglePasswordVisibility -> {
                _uiState.update {
                    it.copy(isPasswordVisible = !it.isPasswordVisible)
                }
            }

            RegistrationEvent.ToggleConfirmPasswordVisibility -> {
                _uiState.update {
                    it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible)
                }
            }

            RegistrationEvent.ContinueClicked -> {
                submit()

            }
        }
    }

    private fun updateButtonState() { //Доступ к кнопке
        val state = _uiState.value

        val isNameFilledCorrectly =
            state.nickname.trim().length in 2..50

        val isFormFilled =
            state.phone.isNotBlank() &&
                    isNameFilledCorrectly &&
                    state.password.isNotBlank() &&
                    state.confirmPassword.isNotBlank()

        _uiState.update {
            it.copy(isContinueEnabled = isFormFilled)
        }
    }

    private fun submit() {
        val state = _uiState.value
        val trimmedName = state.nickname.trim()

        val phoneError =
            if (state.phone.length != 10) "Введите номер полностью" else null

        val fullNameError =
            when {
                trimmedName.length < 2 -> "Имя должно содержать минимум 2 символа"
                trimmedName.length > 50 -> "Имя не должно быть длиннее 50 символов"
                !nickNameRegex.matches(trimmedName) ->
                    "Имя может содержать только буквы, пробелы и дефис"
                else -> null
            }

        val passwordError =
            when{
                state.password.length < 6 -> "Пароль должен быть не менее 6 символов"
                !passwordRegex.matches(state.password) -> "Пароль может содержать только латинские буквы и спец.символы"
                else -> null
            }

        val confirmPasswordError =
            when {
                state.confirmPassword.isBlank() -> "Подтвердите пароль"
                state.password != state.confirmPassword -> "Пароли не совпадают"
                else -> null
            }

        _uiState.update {
            it.copy(
                phoneError = phoneError,
                fullNameError = fullNameError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError
            )
        }

        val hasErrors = listOf(
            phoneError,
            fullNameError,
            passwordError,
            confirmPasswordError
        ).any { it != null }

        if (hasErrors) return

        checkPhoneAndContinue()
    }

    private fun checkPhoneAndContinue() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // Здесь будет вызов repository / network на проверку одинакового номера в БД
                // val isPhoneAvailable = repository.isPhoneAvailable(_uiState.value.phone)
                val isPhoneAvailable = true

                if (!isPhoneAvailable) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            phoneError = "Пользователь с таким номером уже существует"
                        )
                    }
                    return@launch
                }

                _uiState.update { it.copy(isLoading = false) }
                _actions.emit(RegistrationAction.NavigateToCatalog)

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _actions.emit(
                    RegistrationAction.ShowError("Ошибка сети. Попробуйте ещё раз")
                )
            }
        }
    }
}