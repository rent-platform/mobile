package com.example.session

import kotlinx.coroutines.flow.Flow

interface SessionManager {
    val isAuthorized: Flow<Boolean>

    suspend fun logout()
    suspend fun getAccessToken(): String?
}