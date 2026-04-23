package com.example.network.di

import com.example.auth.data.api.AuthApi
import com.example.auth.data.local.TokenStorage
import com.example.network.auth.AuthInterceptor
import com.example.network.auth.TokenAuthenticator
import com.example.network.auth.UserAgentInterceptor
import com.example.network.auth.provideHttpLoggingInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val networkModule = module {

    single {
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
            isLenient = true
        }
    }

    single<HttpLoggingInterceptor> {
        provideHttpLoggingInterceptor()
    }

    single {
        UserAgentInterceptor()
    }

    single {
        AuthInterceptor(
            tokenStorage = get<TokenStorage>()
        )
    }

    //Отдельный клиент для refresh
    single(named("refreshOkHttp")) {
        OkHttpClient.Builder()
            .addInterceptor(get<UserAgentInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single(named("refreshRetrofit")) {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8181/")
            .client(get<OkHttpClient>(named("refreshOkHttp")))
            .addConverterFactory(
                get<Json>().asConverterFactory("application/json".toMediaType())
            )
            .build()
    }

    single(named("refreshAuthApi")) {
        get<Retrofit>(named("refreshRetrofit")).create(AuthApi::class.java)
    }

    single {
        TokenAuthenticator(
            tokenStorage = get<TokenStorage>(),
            refreshAuthApi = get(named("refreshAuthApi"))
        )
    }

    single(named("mainOkHttp")) {
        OkHttpClient.Builder()
            .addInterceptor(get<UserAgentInterceptor>())
            .addInterceptor(get<AuthInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .authenticator(get<TokenAuthenticator>())
            .build()
    }

    single(named("mainRetrofit")) {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8181/")
            .client(get<OkHttpClient>(named("mainOkHttp")))
            .addConverterFactory(
                get<Json>().asConverterFactory("application/json".toMediaType())
            )
            .build()
    }
}