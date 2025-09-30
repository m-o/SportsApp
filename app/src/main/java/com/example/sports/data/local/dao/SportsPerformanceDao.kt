package com.example.sports.data.local.dao

import androidx.room.*
import com.example.sports.data.local.entity.SportsPerformanceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SportsPerformanceDao {

    @Query("SELECT * FROM sports_performances ORDER BY createdAt DESC")
    fun getAllPerformances(): Flow<List<SportsPerformanceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerformance(performance: SportsPerformanceEntity): Long

    // Return Int, count rows deleted
    @Delete
    suspend fun deletePerformance(performance: SportsPerformanceEntity)

}