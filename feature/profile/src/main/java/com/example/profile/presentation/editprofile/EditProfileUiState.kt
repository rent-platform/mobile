package com.example.profile.presentation.editprofile

data class EditProfileUiState(
    val nickname: String = "",
    val fullName: String = "",
    val email: String = "",
    val bio: String = "",
    val avatarUrl: String? = null,

    val nicknameError: String? = null,
    val fullNameError: String? = null,
    val emailError: String? = null,
    val bioError: String? = null,

    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)