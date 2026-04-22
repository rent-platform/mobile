package com.example.auth.domain

data class User(
    val id: String,
    val email: String?,
    val phone: String?,
    val fullName: String,
    val nickname: String,
    val avatarUrl: String?,
    val bio: String?,
    val role: UserRole,
    val isActive: Boolean
)