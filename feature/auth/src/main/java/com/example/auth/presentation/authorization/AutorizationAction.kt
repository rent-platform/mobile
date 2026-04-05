package com.example.auth.presentation.authorization

sealed interface AuthorizationAction {
    data object NavigateToCatalog : AuthorizationAction
    data object NavigateToRegistration : AuthorizationAction
    data class ShowError(val message: String) : AuthorizationAction
}