package com.example.deals.domain

import com.example.deals.presentation.DealListItemUi

interface DealsRepository {

    suspend fun getRenterDeals(): List<DealListItemUi>

    suspend fun getOwnerDeals(): List<DealListItemUi>
}