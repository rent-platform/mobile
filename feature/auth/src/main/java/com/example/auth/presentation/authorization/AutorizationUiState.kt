package com.example.auth.presentation.authorization

data class AuthorizationUiState(
    val login: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoginEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val loginError: String? = null,
    val passwordError: String? = null
)