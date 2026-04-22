package com.example.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDto(
    val id: String,
    val email: String? = null,
    val phone: String? = null,
    val fullName: String,
    val nickname: String,
    val avatarUrl: String? = null,
    val bio: String? = null,
    val role: String,
    val isActive: Boolean
)