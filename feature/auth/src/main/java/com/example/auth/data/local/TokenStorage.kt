package com.example.auth.data.local

import com.example.auth.domain.AuthTokens
import kotlinx.coroutines.flow.Flow

interface TokenStorage {
    val isAuthorized: Flow<Boolean>

    suspend fun saveTokens(tokens: AuthTokens)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun clear()
}