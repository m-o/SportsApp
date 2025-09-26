package com.example.sports.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sports.data.local.dao.SportsPerformanceDao
import com.example.sports.data.local.entity.SportsPerformanceEntity

@Database(
    entities = [SportsPerformanceEntity::class],
    version = 2,
    exportSchema = false
)
abstract class SportsDatabase : RoomDatabase() {
    abstract fun sportsPerformanceDao(): SportsPerformanceDao
}