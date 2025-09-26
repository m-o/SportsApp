package com.example.sports

import android.app.Application
import com.example.sports.di.appModule
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class SportsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Timber
        Timber.plant(Timber.DebugTree())

        // Enable Firebase offline persistence
        Firebase.database.setPersistenceEnabled(true)

        startKoin {
            androidContext(this@SportsApplication)
            modules(appModule)
        }
    }
}