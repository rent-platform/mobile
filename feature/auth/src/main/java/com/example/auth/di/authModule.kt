package com.example.auth.di

import com.example.auth.data.api.AuthApi
import com.example.auth.data.api.UserApi
import com.example.auth.data.local.DataStoreTokenStorage
import com.example.auth.data.local.TokenStorage
import com.example.auth.data.repository.AuthRepositoryImpl
import com.example.auth.domain.repository.AuthRepository
import com.example.auth.presentation.authorization.AutorizationViewModel
import com.example.auth.presentation.registration.RegistrationViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit

val authModule = module {
    single { get<Retrofit>().create(AuthApi::class.java) }
    single { get<Retrofit>().create(UserApi::class.java) }

    single<TokenStorage> { DataStoreTokenStorage(get()) }

    single<AuthRepository> {
        AuthRepositoryImpl(
            authApi = get(),
            userApi = get(),
            tokenStorage = get()
        )
    }

    viewModelOf(::AutorizationViewModel)
    viewModelOf(::RegistrationViewModel)
}