package com.example.auth.domain

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: Long
)