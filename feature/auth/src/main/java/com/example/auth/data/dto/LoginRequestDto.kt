package com.example.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val login: String,
    val password: String,
    val rememberMe: Boolean
)