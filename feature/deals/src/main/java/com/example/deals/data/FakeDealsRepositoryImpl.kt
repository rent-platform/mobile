package com.example.deals.data

import com.example.core.demo.DemoScenario
import com.example.deals.domain.DealsRepository
import com.example.deals.presentation.DealListItemUi
import com.example.deals.presentation.toDealListItemUi
import kotlinx.coroutines.delay

class FakeDealsRepositoryImpl : DealsRepository {

    override suspend fun getRenterDeals(): List<DealListItemUi> {
        delay(300)

        return DemoScenario.myRenterDeals
            .sortedByDescending { it.createdAt }
            .map { deal -> deal.toDealListItemUi() }
    }

    override suspend fun getOwnerDeals(): List<DealListItemUi> {
        delay(300)

        return DemoScenario.myOwnerDeals
            .sortedByDescending { it.createdAt }
            .map { deal -> deal.toDealListItemUi() }
    }
}