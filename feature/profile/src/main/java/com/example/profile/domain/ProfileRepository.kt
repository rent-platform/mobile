package com.example.profile.domain

import com.example.profile.presentation.profile.ProfileUiState

interface ProfileRepository {

    suspend fun getMyProfile(): ProfileUiState

    suspend fun logout()

    suspend fun updateMyProfile(
        fullName: String,
        nickname: String?,
        email: String?,
        bio: String?,
        avatarUrl: String?
    ): ProfileUiState
}