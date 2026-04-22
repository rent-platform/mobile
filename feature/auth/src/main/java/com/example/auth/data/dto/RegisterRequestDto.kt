package com.example.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
    val phone: String,
    val password: String,
    val confirmPassword: String,
    val nickname: String
)