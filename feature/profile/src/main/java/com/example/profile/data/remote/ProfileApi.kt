package com.example.profile.data.remote

import com.example.profile.data.remote.dto.UserResponseDto
import retrofit2.http.GET

interface ProfileApi {

    @GET("/api/users/me")
    suspend fun getMe(): UserResponseDto
}