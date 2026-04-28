package com.example.profile.di

import com.example.profile.data.remote.ProfileApi
import com.example.profile.data.repository.ProfileRepositoryImpl
import com.example.profile.domain.ProfileRepository
import com.example.profile.presentation.editprofile.EditProfileViewModel
import com.example.profile.presentation.profile.ProfileViewModel
import com.example.profile.presentation.profilesettings.ProfileSettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val profileModule = module {

    single<ProfileApi> {
        get<Retrofit>(named("mainRetrofit")).create(ProfileApi::class.java)
    }

    single<ProfileRepository> {
        ProfileRepositoryImpl(
            api = get(),
            sessionManager = get()
        )
    }

    viewModel {
        ProfileViewModel(
            repository = get()
        )
    }
    viewModel {
        EditProfileViewModel(
            repository = get()
        )
    }
    viewModel {
        ProfileSettingsViewModel(
            repository = get()
        )
    }
}