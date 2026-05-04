package com.example.marketplace.data.search

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.marketplaceSearchHistoryDataStore by preferencesDataStore(
    name = "marketplace_search_history"
)

class SearchHistoryRepositoryImpl(
    private val context: Context
) : SearchHistoryRepository {

    override fun observeHistory(): Flow<List<String>> {
        return context.marketplaceSearchHistoryDataStore.data.map { preferences ->
            preferences[SEARCH_HISTORY_KEY]
                .orEmpty()
                .toHistoryList()
        }
    }

    override suspend fun addQuery(query: String) {
        val normalizedQuery = query.trim()

        if (normalizedQuery.isBlank()) return

        context.marketplaceSearchHistoryDataStore.edit { preferences ->
            val currentHistory = preferences[SEARCH_HISTORY_KEY]
                .orEmpty()
                .toHistoryList()

            val updatedHistory = buildList {
                add(normalizedQuery)

                currentHistory
                    .filterNot { it.equals(normalizedQuery, ignoreCase = true) }
                    .forEach(::add)
            }.take(MAX_HISTORY_SIZE)

            preferences[SEARCH_HISTORY_KEY] = updatedHistory.toStorageString()
        }
    }

    override suspend fun removeQuery(query: String) {
        context.marketplaceSearchHistoryDataStore.edit { preferences ->
            val currentHistory = preferences[SEARCH_HISTORY_KEY]
                .orEmpty()
                .toHistoryList()

            val updatedHistory = currentHistory.filterNot {
                it.equals(query, ignoreCase = true)
            }

            if (updatedHistory.isEmpty()) {
                preferences.remove(SEARCH_HISTORY_KEY)
            } else {
                preferences[SEARCH_HISTORY_KEY] = updatedHistory.toStorageString()
            }
        }
    }

    override suspend fun clearHistory() {
        context.marketplaceSearchHistoryDataStore.edit { preferences ->
            preferences.remove(SEARCH_HISTORY_KEY)
        }
    }

    private fun String.toHistoryList(): List<String> {
        if (isBlank()) return emptyList()

        return split(HISTORY_SEPARATOR)
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinctBy { it.lowercase() }
            .take(MAX_HISTORY_SIZE)
    }

    private fun List<String>.toStorageString(): String {
        return joinToString(HISTORY_SEPARATOR)
    }

    private companion object {
        const val MAX_HISTORY_SIZE = 15
        const val HISTORY_SEPARATOR = "|||"

        val SEARCH_HISTORY_KEY = stringPreferencesKey("search_history")
    }
}