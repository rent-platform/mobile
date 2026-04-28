package com.example.profile.data.repository

import android.util.Log
import com.example.profile.data.mapper.toProfileUiState
import com.example.profile.data.mock.MockProfileExtra
import com.example.profile.data.mock.MockProfileStats
import com.example.profile.data.remote.ProfileApi
import com.example.profile.data.remote.dto.ChangePasswordRequestDto
import com.example.profile.data.remote.dto.UpdateProfileRequestDto
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

    override suspend fun updateMyProfile(
        fullName: String,
        nickname: String?,
        email: String?,
        bio: String?,
        avatarUrl: String?
    ): ProfileUiState {
        val updatedUser = api.updateMe(
            request = UpdateProfileRequestDto(
                fullName = fullName,
                nickname = nickname,
                email = email,
                bio = bio,
                avatarUrl = avatarUrl
            )
        )

        return updatedUser.toProfileUiState()
    }

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): String {
        val response = api.changePassword(
            request = ChangePasswordRequestDto(
                currentPassword = currentPassword,
                newPassword = newPassword,
                confirmNewPassword = confirmNewPassword
            )
        )

        return response.message
    }

    override suspend fun deleteMyProfile(): String {
        val response = api.deleteMe()

        sessionManager.clearSession()

        return response.message
    }
}