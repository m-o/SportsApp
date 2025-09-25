package com.example.sports.data.local.dao

import androidx.room.*
import com.example.sports.data.local.entity.SportsPerformanceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SportsPerformanceDao {

    @Query("SELECT * FROM sports_performances ORDER BY createdAt DESC")
    fun getAllPerformances(): Flow<List<SportsPerformanceEntity>>

    @Query("SELECT * FROM sports_performances WHERE storageType = :storageType ORDER BY createdAt DESC")
    fun getPerformancesByType(storageType: String): Flow<List<SportsPerformanceEntity>>

    @Query("SELECT * FROM sports_performances WHERE id = :id")
    suspend fun getPerformanceById(id: Long): SportsPerformanceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerformance(performance: SportsPerformanceEntity): Long

    @Update
    suspend fun updatePerformance(performance: SportsPerformanceEntity)

    @Delete
    suspend fun deletePerformance(performance: SportsPerformanceEntity)

    @Query("DELETE FROM sports_performances")
    suspend fun deleteAllPerformances()
}