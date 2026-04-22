package com.example.auth.data.api

import com.example.auth.data.dto.LoginRequestDto
import com.example.auth.data.dto.RefreshTokenRequestDto
import com.example.auth.data.dto.RegisterRequestDto
import com.example.auth.data.dto.TokenResponseDto
import com.example.auth.data.dto.UserResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/api/auth/register")
    suspend fun register(
        @Body body: RegisterRequestDto
    ): UserResponseDto

    @POST("/api/auth/login")
    suspend fun login(
        @Body body: LoginRequestDto
    ): TokenResponseDto

    @POST("/api/auth/refresh")
    suspend fun refresh(
        @Body body: RefreshTokenRequestDto
    ): TokenResponseDto

    @POST("/api/auth/logout")
    suspend fun logout(
        @Body body: RefreshTokenRequestDto
    )
}