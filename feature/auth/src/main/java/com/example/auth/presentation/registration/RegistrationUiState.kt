package com.example.auth.presentation.registration

data class RegistrationUiState(
    val phone: String = "",
    val nickname: String = "",
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