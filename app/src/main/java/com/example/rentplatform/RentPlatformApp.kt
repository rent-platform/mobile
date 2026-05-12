package com.example.rentplatform

import android.app.Application
import com.example.auth.data.local.authStorageModule
import com.example.auth.di.authModule
import com.example.deals.di.dealsModule
import com.example.marketplace.di.marketplaceModule
import com.example.network.di.networkModule
import com.example.profile.di.profileModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RentPlatformApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@RentPlatformApp)
            modules(
                networkModule,
                authStorageModule,
                authModule,
                profileModule,
                marketplaceModule,
                dealsModule
            )
        }
    }
}