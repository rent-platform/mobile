package com.example.auth.presentation.registration

sealed interface RegistrationAction {
    data object NavigateToCatalog : RegistrationAction
    data class ShowError(val message: String) : RegistrationAction
}