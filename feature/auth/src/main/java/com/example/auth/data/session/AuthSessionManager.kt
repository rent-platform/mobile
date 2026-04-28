package com.example.auth.data.session

import com.example.auth.domain.repository.AuthRepository
import com.example.session.SessionManager
import kotlinx.coroutines.flow.Flow

class AuthSessionManager(
    private val authRepository: AuthRepository
) : SessionManager {

    override val isAuthorized: Flow<Boolean>
        get() = authRepository.isAuthorized

    override suspend fun logout() {
        authRepository.logout()
    }

    override suspend fun clearSession() {
        authRepository.clearLocalSession()
    }

    override suspend fun getAccessToken(): String? {
        return authRepository.getAccessToken()
    }
}