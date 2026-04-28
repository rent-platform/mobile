package com.example.session

import kotlinx.coroutines.flow.Flow

interface SessionManager {
    val isAuthorized: Flow<Boolean>

    suspend fun logout()
    suspend fun clearSession()
    suspend fun getAccessToken(): String?
}