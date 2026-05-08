package com.example.marketplace.domain.repository

import com.example.marketplace.domain.model.CatalogData
import com.example.marketplace.domain.model.ItemDetails

interface CatalogRepository {

    suspend fun getCatalog(): CatalogData

    suspend fun getItemDetails(itemId: String): ItemDetails?

    suspend fun toggleFavorite(itemId: String): Boolean
}