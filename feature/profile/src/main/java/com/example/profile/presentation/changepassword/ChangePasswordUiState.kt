package com.example.profile.presentation.changepassword

data class ChangePasswordUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",

    val currentPasswordError: String? = null,
    val newPasswordError: String? = null,
    val confirmNewPasswordError: String? = null,

    val generalError: String? = null,
    val isSaving: Boolean = false
) {
    val canSave: Boolean
        get() = !isSaving &&
                currentPassword.isNotBlank() &&
                newPassword.isNotBlank() &&
                confirmNewPassword.isNotBlank()
}