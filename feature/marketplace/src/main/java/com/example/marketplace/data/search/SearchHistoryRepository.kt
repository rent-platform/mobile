package com.example.marketplace.data.search

import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {

    fun observeHistory(): Flow<List<String>>

    suspend fun addQuery(query: String)

    suspend fun removeQuery(query: String)

    suspend fun clearHistory()
}