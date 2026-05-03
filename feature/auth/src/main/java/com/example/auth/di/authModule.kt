package com.example.auth.di

import com.example.auth.data.api.AuthApi
import com.example.auth.data.api.UserApi
import com.example.auth.data.local.DataStoreTokenStorage
import com.example.auth.data.local.TokenStorage
import com.example.auth.data.repository.AuthRepositoryImpl
import com.example.auth.data.session.AuthSessionManager
import com.example.auth.domain.repository.AuthRepository
import com.example.auth.presentation.authorization.AutorizationViewModel
import com.example.auth.presentation.registration.RegistrationViewModel
import com.example.session.SessionManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val authModule = module {
    single<AuthApi> {
        get<Retrofit>(named("mainRetrofit")).create(AuthApi::class.java)
    }

    single<UserApi> {
        get<Retrofit>(named("mainRetrofit")).create(UserApi::class.java)
    }

    single<TokenStorage> { DataStoreTokenStorage(get()) }

    single<AuthRepository> {
        AuthRepositoryImpl(
            authApi = get(),
            userApi = get(),
            tokenStorage = get()
        )
    }

    single<SessionManager> {
        AuthSessionManager(
            authRepository = get()
        )
    }

    viewModel {
        AutorizationViewModel(
            authRepository = get()
        )
    }

    viewModel {
        RegistrationViewModel(
            authRepository = get()
        )
    }
}