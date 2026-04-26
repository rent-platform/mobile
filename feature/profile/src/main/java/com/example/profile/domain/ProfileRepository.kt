package com.example.profile.domain

import com.example.profile.presentation.profile.ProfileUiState

interface ProfileRepository {

    suspend fun getMyProfile(): ProfileUiState

    suspend fun logout()
}