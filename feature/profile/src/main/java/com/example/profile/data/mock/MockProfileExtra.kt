package com.example.profile.data.mock

data class MockProfileExtra(
    val isPhoneVerified: Boolean = true,
    val isEmailVerified: Boolean = true,
    val registeredAt: String = "12.04.2026",
    val updatedAt: String? = "25.04.2026"
)