package com.example.network.auth

import com.example.auth.data.api.AuthApi
import com.example.auth.data.dto.RefreshTokenRequestDto
import com.example.auth.data.local.TokenStorage
import com.example.auth.domain.AuthTokens
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val tokenStorage: TokenStorage,
    private val refreshAuthApi: AuthApi
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null

        val refreshToken = runBlocking {
            tokenStorage.getRefreshToken()
        } ?: return null

        val newTokens = runBlocking {
            try {
                refreshAuthApi.refresh(
                    RefreshTokenRequestDto(refreshToken)
                )
            } catch (e: Exception) {
                null
            }
        } ?: run {
            runBlocking { tokenStorage.clear() }
            return null
        }

        runBlocking {
            tokenStorage.saveTokens(
                AuthTokens(
                    accessToken = newTokens.accessToken,
                    refreshToken = newTokens.refreshToken,
                    tokenType = newTokens.tokenType,
                    expiresIn = newTokens.expiresIn
                )
            )
        }

        return response.request.newBuilder()
            .header("Authorization", "Bearer ${newTokens.accessToken}")
            .build()
    }

    //Чтобы не уйти в бесконечный цикл
    private fun responseCount(response: Response): Int {
        var result = 1
        var current = response.priorResponse

        while (current != null) {
            result++
            current = current.priorResponse
        }

        return result
    }
}