package com.example.profile.data.remote

import com.example.profile.data.remote.dto.UpdateProfileRequestDto
import com.example.profile.data.remote.dto.UserResponseDto
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Body

interface ProfileApi {

    @GET("/api/users/me")
    suspend fun getMe(): UserResponseDto

    @PUT("/api/users/me")
    suspend fun updateMe(
        @Body request: UpdateProfileRequestDto
    ): UserResponseDto
}