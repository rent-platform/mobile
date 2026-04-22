package com.example.auth.domain

data class AuthorizedUser(
    val user: User,
    val tokens: AuthTokens
)