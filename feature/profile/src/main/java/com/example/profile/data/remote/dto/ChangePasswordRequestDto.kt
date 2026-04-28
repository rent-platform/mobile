package com.example.profile.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequestDto(
    val currentPassword: String,
    val newPassword: String,
    val confirmNewPassword: String
)