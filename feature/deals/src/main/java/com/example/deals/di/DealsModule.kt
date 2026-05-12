package com.example.deals.di

import com.example.deals.data.FakeDealsRepositoryImpl
import com.example.deals.domain.DealsRepository
import com.example.deals.presentation.DealsViewModel
import com.example.deals.presentation.dealdetails.DealDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dealsModule = module {

    single<DealsRepository> {
        FakeDealsRepositoryImpl()
    }

    viewModel {
        DealsViewModel(
            dealsRepository = get()
        )
    }

    viewModel { parameters ->
        DealDetailsViewModel(
            dealId = parameters.get(),
            dealsRepository = get()
        )
    }
}