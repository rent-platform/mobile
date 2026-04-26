package com.example.profile.data.repository

import android.util.Log
import com.example.profile.data.mapper.toProfileUiState
import com.example.profile.data.mock.MockProfileExtra
import com.example.profile.data.mock.MockProfileStats
import com.example.profile.data.remote.ProfileApi
import com.example.profile.domain.ProfileRepository
import com.example.profile.presentation.profile.ProfileUiState
import com.example.session.SessionManager

class ProfileRepositoryImpl(
    private val api: ProfileApi,
    private val sessionManager: SessionManager
) : ProfileRepository {

    override suspend fun getMyProfile(): ProfileUiState {
        val user = api.getMe()
        return user.toProfileUiState()
    }

    override suspend fun logout() {
        sessionManager.logout()
    }
}