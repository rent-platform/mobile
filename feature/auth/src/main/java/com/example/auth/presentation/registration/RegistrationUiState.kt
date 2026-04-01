package com.example.auth.presentation.registration

data class RegistrationUiState(
    val phone: String = "",
    val fullName: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isContinueEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val phoneError: String? = null,
    val fullNameError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null
)

//События пользователя
sealed interface RegistrationEvent {
    data class PhoneChanged(val value: String) : RegistrationEvent
    data class FullNameChanged(val value: String) : RegistrationEvent
    data class PasswordChanged(val value: String) : RegistrationEvent
    data class ConfirmPasswordChanged(val value: String) : RegistrationEvent

    data object TogglePasswordVisibility : RegistrationEvent
    data object ToggleConfirmPasswordVisibility : RegistrationEvent

    data object ContinueClicked : RegistrationEvent
    data object BackClicked : RegistrationEvent
}

//Одноразовые действия
sealed interface RegistrationAction {
    data object NavigateToCatalog : RegistrationAction
    data object NavigateBack : RegistrationAction
}