package com.example.profile.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDto(
    val id: String,
    val email: String?,
    val phone: String,
    val fullName: String,
    val nickname: String?,
    val avatarUrl: String?,
    val bio: String?,
    val role: String,
    val isActive: Boolean
)