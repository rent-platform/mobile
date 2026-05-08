package com.example.core.demo.model

data class DemoUser(
    val id: String,
    val fullName: String,
    val nickname: String,
    val avatarUrl: String?,
    val bio: String?,
    val phone: String?,
    val email: String?,
    val role: String = "user",
    val rating: Float,
    val reviewsCount: Int,
    val isPhoneVerified: Boolean = true,
    val isEmailVerified: Boolean = true,
    val isActive: Boolean = true,
    val registeredAt: String,
    val updatedAt: String?
)