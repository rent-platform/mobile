package com.example.profile.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequestDto(
    val fullName: String,
    val nickname: String?,
    val email: String?,
    val bio: String?,
    val avatarUrl: String?
)