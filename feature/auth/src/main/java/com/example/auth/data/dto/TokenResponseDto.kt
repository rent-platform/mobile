package com.example.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponseDto(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: Long
)