package com.example.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequestDto(
    val refreshToken: String
)