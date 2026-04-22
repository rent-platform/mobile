package com.example.auth.domain.repository

import com.example.auth.domain.AuthTokens
import com.example.auth.domain.AuthorizedUser
import com.example.auth.domain.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val isAuthorized: Flow<Boolean>

    suspend fun register(
        phone: String,
        nickname: String,
        password: String,
        confirmPassword: String
    ): Result<User>

    suspend fun login(
        login: String,
        password: String,
        rememberMe: Boolean
    ): Result<AuthorizedUser>

    suspend fun refreshTokens(): Result<AuthTokens>

    suspend fun getCurrentUser(): Result<User>

    suspend fun logout()

    suspend fun clearLocalSession()

    suspend fun getAccessToken(): String?
}