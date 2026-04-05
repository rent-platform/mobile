package com.example.auth.presentation.authorization

sealed interface AuthorizationEvent {
    data class PhoneChanged(val value: String) : AuthorizationEvent
    data class PasswordChanged(val value: String) : AuthorizationEvent

    data object TogglePasswordVisibility : AuthorizationEvent
    data object LoginClicked : AuthorizationEvent
    data object RegisterClicked : AuthorizationEvent
}