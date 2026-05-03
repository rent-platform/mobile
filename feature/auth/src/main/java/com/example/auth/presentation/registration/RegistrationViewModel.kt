package com.example.auth.presentation.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class RegistrationViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    private val _actions = MutableSharedFlow<RegistrationAction>()
    val actions = _actions.asSharedFlow()

    private val nickNameRegex =
        Regex("^[а-яёА-ЯЁa-zA-Z]+(?:[ -][а-яёА-ЯЁa-zA-Z]+)*$")

    private val passwordRegex = Regex("^[A-Za-z\\d@#$%^&+=!]{8,20}$")

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

            is RegistrationEvent.NicknameChanged -> {
                _uiState.update {
                    it.copy(
                        nickname = event.value,
                        nicknameError = null
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
                if (submit()) {
                    register()
                }
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

    private fun submit(): Boolean {
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
                nicknameError = fullNameError,
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

        return !hasErrors
    }

    private fun register() {
        viewModelScope.launch {
            val state = _uiState.value

            _uiState.update { it.copy(isLoading = true) }

            val normalizedPhone = "+7${state.phone}"

            authRepository.register(
                phone = normalizedPhone,
                nickname = state.nickname.trim(),
                password = state.password,
                confirmPassword = state.confirmPassword
            ).onSuccess {
                //Логиним после успешной регистрации
                authRepository.login(
                    login = normalizedPhone,
                    password = state.password,
                    rememberMe = true
                ).onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    _actions.emit(RegistrationAction.AuthSuccess)
                }.onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                    _actions.emit(
                        RegistrationAction.ShowError(
                            "Аккаунт создан, но автоматический вход не выполнен"
                        )
                    )
                }
            }.onFailure { throwable ->
                _uiState.update { it.copy(isLoading = false) }

                when (throwable) {
                    is HttpException -> {
                        when (throwable.code()) {
                            400 -> _actions.emit(
                                RegistrationAction.ShowError("Проверьте корректность данных")
                            )
                            409 -> {
                                _uiState.update {
                                    it.copy(phoneError = "Пользователь с такими данными уже существует")
                                }
                            }
                            else -> _actions.emit(
                                RegistrationAction.ShowError("Ошибка сервера. Попробуйте позже")
                            )
                        }
                    }

                    is IOException -> {
                        _actions.emit(
                            RegistrationAction.ShowError("Ошибка сети. Проверьте подключение")
                        )
                    }

                    else -> {
                        _actions.emit(
                            RegistrationAction.ShowError("Не удалось выполнить регистрацию")
                        )
                    }
                }
            }
        }
    }
}