package com.example.auth.presentation.registration

sealed interface RegistrationAction {
    data object NavigateToCatalog : RegistrationAction
    data object NavigateBack : RegistrationAction
}