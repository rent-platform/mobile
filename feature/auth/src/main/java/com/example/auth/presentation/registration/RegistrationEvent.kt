package com.example.auth.presentation.registration

sealed interface RegistrationEvent {
    data class PhoneChanged(val value: String) : RegistrationEvent
    data class NicknameChanged(val value: String) : RegistrationEvent
    data class PasswordChanged(val value: String) : RegistrationEvent
    data class ConfirmPasswordChanged(val value: String) : RegistrationEvent

    data object TogglePasswordVisibility : RegistrationEvent
    data object ToggleConfirmPasswordVisibility : RegistrationEvent

    data object ContinueClicked : RegistrationEvent
}