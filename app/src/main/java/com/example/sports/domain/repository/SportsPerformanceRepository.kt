package com.example.sports.domain.repository

import com.example.sports.domain.model.SportsPerformance
import com.example.sports.domain.model.StorageType
import kotlinx.coroutines.flow.Flow

interface SportsPerformanceRepository {
    fun getAllPerformances(): Flow<List<SportsPerformance>>
    fun getPerformancesByType(storageType: StorageType): Flow<List<SportsPerformance>>
    suspend fun insertPerformance(performance: SportsPerformance): Long
    suspend fun deletePerformance(performance: SportsPerformance)
}