package com.example.marketplace.di

import com.example.marketplace.data.repository.FakeCatalogRepositoryImpl
import com.example.marketplace.data.search.SearchHistoryRepository
import com.example.marketplace.data.search.SearchHistoryRepositoryImpl
import com.example.marketplace.domain.repository.CatalogRepository
import com.example.marketplace.presentation.catalog.CatalogViewModel
import com.example.marketplace.presentation.itemdetails.ItemDetailsViewModel
import com.example.marketplace.presentation.listing.CreateListingViewModel
import com.example.marketplace.presentation.rentrequest.RentRequestViewModel
import com.example.marketplace.presentation.search.filters.SearchFiltersViewModel
import com.example.marketplace.presentation.search.input.SearchInputViewModel
import com.example.marketplace.presentation.search.results.SearchResultsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val marketplaceModule = module {

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(
            context = androidContext()
        )
    }

    single<CatalogRepository> {
        FakeCatalogRepositoryImpl()
    }

    viewModel {
        CatalogViewModel(
            catalogRepository = get()
        )
    }

    viewModel {
        ItemDetailsViewModel(
            catalogRepository = get()
        )
    }

    viewModel {
        SearchInputViewModel(
            searchHistoryRepository = get()
        )
    }

    viewModel { parameters ->
        SearchResultsViewModel(
            initialQuery = parameters.get(),
            catalogRepository = get()
        )
    }
    viewModel {
        SearchFiltersViewModel(
            catalogRepository = get()
        )
    }
    viewModel {
        RentRequestViewModel(
            catalogRepository = get()
        )
    }
    viewModel {
        CreateListingViewModel(
            catalogRepository = get()
        )
    }
}