package com.example.auth.presentation.registration

sealed interface RegistrationAction {
    data object AuthSuccess : RegistrationAction
    data class ShowError(val message: String) : RegistrationAction
}