package com.example.marketplace.domain.repository

import com.example.marketplace.domain.model.CatalogCategory
import com.example.marketplace.domain.model.CatalogData
import com.example.marketplace.domain.model.CatalogItem
import com.example.marketplace.domain.model.CatalogSearchParams
import com.example.marketplace.domain.model.ItemDetails

interface CatalogRepository {

    suspend fun getCatalog(): CatalogData

    suspend fun getItemDetails(itemId: String): ItemDetails?

    suspend fun searchItems(params: CatalogSearchParams): List<CatalogItem>
    suspend fun toggleFavorite(itemId: String): Boolean

    suspend fun getCategories(): List<CatalogCategory>
}