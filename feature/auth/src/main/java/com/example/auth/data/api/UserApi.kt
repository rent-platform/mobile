package com.example.auth.data.api

import com.example.auth.data.dto.UserResponseDto
import retrofit2.http.GET

interface UserApi {

    @GET("/api/users/me")
    suspend fun getMe(): UserResponseDto
}