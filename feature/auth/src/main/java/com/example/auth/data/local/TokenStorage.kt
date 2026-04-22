package com.example.auth.data.local

import com.example.auth.domain.AuthTokens

interface TokenStorage {
    suspend fun saveTokens(tokens: AuthTokens)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun clear()
}