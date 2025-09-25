package com.example.sports.di

import com.example.sports.data.local.database.SportsDatabase
import com.example.sports.data.remote.datasource.FirebaseSportsPerformanceDataSource
import com.example.sports.data.repository.SportsPerformanceRepositoryImpl
import com.example.sports.domain.repository.SportsPerformanceRepository
import com.example.sports.presentation.add.AddPerformanceViewModel
import com.example.sports.presentation.list.PerformanceListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Database
    single { SportsDatabase.getDatabase(androidContext()) }

    // DAO
    single { get<SportsDatabase>().sportsPerformanceDao() }

    // Firebase Data Source
    single { FirebaseSportsPerformanceDataSource() }

    // Repository
    single<SportsPerformanceRepository> {
        SportsPerformanceRepositoryImpl(
            localDao = get(),
            firebaseDataSource = get()
        )
    }

    // ViewModels
    viewModel { PerformanceListViewModel(get()) }
    viewModel { AddPerformanceViewModel(get()) }
}