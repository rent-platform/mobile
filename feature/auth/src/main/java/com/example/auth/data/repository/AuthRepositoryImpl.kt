package com.example.auth.data.repository

import com.example.auth.data.api.AuthApi
import com.example.auth.data.api.UserApi
import com.example.auth.data.dto.LoginRequestDto
import com.example.auth.data.dto.RefreshTokenRequestDto
import com.example.auth.data.dto.RegisterRequestDto
import com.example.auth.data.local.TokenStorage
import com.example.auth.data.toDomain
import com.example.auth.domain.AuthTokens
import com.example.auth.domain.AuthorizedUser
import com.example.auth.domain.User
import com.example.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val userApi: UserApi,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override val isAuthorized: Flow<Boolean> = tokenStorage.isAuthorized

    override suspend fun register(
        phone: String,
        nickname: String,
        password: String,
        confirmPassword: String
    ): Result<User> = runCatching {
        authApi.register(
            RegisterRequestDto(
                phone = phone,
                password = password,
                confirmPassword = confirmPassword,
                nickname = nickname
            )
        ).toDomain()
    }

    override suspend fun login(
        login: String,
        password: String,
        rememberMe: Boolean
    ): Result<AuthorizedUser> = runCatching {
        val tokens = authApi.login(
            LoginRequestDto(
                login = login,
                password = password,
                rememberMe = rememberMe
            )
        ).toDomain()

        tokenStorage.saveTokens(tokens)

        val user = userApi.getMe().toDomain()

        AuthorizedUser(
            user = user,
            tokens = tokens
        )
    }

    override suspend fun refreshTokens(): Result<AuthTokens> = runCatching {
        val refreshToken = tokenStorage.getRefreshToken()
            ?: error("Refresh token not found")

        val newTokens = authApi.refresh(
            RefreshTokenRequestDto(refreshToken)
        ).toDomain()

        tokenStorage.saveTokens(newTokens)
        newTokens
    }

    override suspend fun getCurrentUser(): Result<User> = runCatching {
        userApi.getMe().toDomain()
    }

    override suspend fun logout() {
        val refreshToken = tokenStorage.getRefreshToken()
        try {
            if (refreshToken != null) {
                authApi.logout(RefreshTokenRequestDto(refreshToken))
            }
        } finally {
            tokenStorage.clear()
        }
    }

    override suspend fun clearLocalSession() {
        tokenStorage.clear()
    }

    override suspend fun getAccessToken(): String? {
        return tokenStorage.getAccessToken()
    }
}