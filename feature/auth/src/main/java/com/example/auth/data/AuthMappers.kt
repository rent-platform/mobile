package com.example.auth.data

import com.example.auth.data.dto.TokenResponseDto
import com.example.auth.data.dto.UserResponseDto
import com.example.auth.domain.AuthTokens
import com.example.auth.domain.User
import com.example.auth.domain.UserRole

private val ruPhoneRegex = Regex("""^(?:\+7|8)\d{10}$""")

fun UserResponseDto.toDomain(): User {
    return User(
        id = id,
        email = email,
        phone = phone,
        fullName = fullName,
        nickname = nickname,
        avatarUrl = avatarUrl,
        bio = bio,
        role = when (role.lowercase()) {
            "moderator" -> UserRole.MODERATOR
            "admin" -> UserRole.ADMIN
            else -> UserRole.USER
        },
        isActive = isActive
    )
}

fun TokenResponseDto.toDomain(): AuthTokens {
    return AuthTokens(
        accessToken = accessToken,
        refreshToken = refreshToken,
        tokenType = tokenType,
        expiresIn = expiresIn
    )
}

fun String.toServerPhoneRu(): String {
    val phone = trim()

    require(ruPhoneRegex.matches(phone)) {
        "Invalid phone number"
    }

    return if (phone.startsWith("8")) {
        "+7${phone.drop(1)}"
    } else {
        phone
    }
}