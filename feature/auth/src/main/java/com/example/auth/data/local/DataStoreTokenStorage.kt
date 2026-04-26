package com.example.auth.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.auth.domain.AuthTokens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreTokenStorage(
    private val dataStore: DataStore<Preferences>
) : TokenStorage {

    private companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val TOKEN_TYPE = stringPreferencesKey("token_type")
    }

    override val isAuthorized: Flow<Boolean> = dataStore.data
        .map { prefs ->
            !prefs[REFRESH_TOKEN].isNullOrBlank()
        }
        .distinctUntilChanged()

    override suspend fun saveTokens(tokens: AuthTokens) {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = tokens.accessToken
            prefs[REFRESH_TOKEN] = tokens.refreshToken
            prefs[TOKEN_TYPE] = tokens.tokenType
        }
    }

    override suspend fun getAccessToken(): String? {
        return dataStore.data.map { it[ACCESS_TOKEN] }.first()
    }

    override suspend fun getRefreshToken(): String? {
        return dataStore.data.map { it[REFRESH_TOKEN] }.first()
    }

    override suspend fun clear() {
        dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN)
            prefs.remove(REFRESH_TOKEN)
            prefs.remove(TOKEN_TYPE)
        }
    }
}