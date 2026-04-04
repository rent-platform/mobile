package com.example.auth.presentation.authorization

data class AuthorizationUiState(
    val phone: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoginEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val phoneError: String? = null,
    val passwordError: String? = null
)