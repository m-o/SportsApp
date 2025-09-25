package com.example.sports

import android.app.Application
import com.example.sports.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class SportsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Timber
        Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@SportsApplication)
            modules(appModule)
        }
    }
}