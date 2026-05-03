package com.example.profile.data.remote

import com.example.profile.data.remote.dto.ChangePasswordRequestDto
import com.example.profile.data.remote.dto.MessageResponseDto
import com.example.profile.data.remote.dto.UpdateProfileRequestDto
import com.example.profile.data.remote.dto.UserResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT

interface ProfileApi {

    @GET("/api/users/me")
    suspend fun getMe(): UserResponseDto

    @PUT("/api/users/me")
    suspend fun updateMe(
        @Body request: UpdateProfileRequestDto
    ): UserResponseDto

    @PUT("/api/users/me/password")
    suspend fun changePassword(
        @Body request: ChangePasswordRequestDto
    ): MessageResponseDto

    @DELETE("/api/users/me")
    suspend fun deleteMe(): MessageResponseDto
}