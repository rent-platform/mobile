package com.example.marketplace.di

import com.example.marketplace.data.search.SearchHistoryRepository
import com.example.marketplace.data.search.SearchHistoryRepositoryImpl
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

    viewModel {
        SearchInputViewModel(
            searchHistoryRepository = get()
        )
    }

    viewModel { parameters ->
        SearchResultsViewModel(
            initialQuery = parameters.get()
        )
    }
    viewModel {
        SearchFiltersViewModel()
    }
}