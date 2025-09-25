package com.example.sports.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.sports.data.local.dao.SportsPerformanceDao
import com.example.sports.data.local.entity.SportsPerformanceEntity

@Database(
    entities = [SportsPerformanceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SportsDatabase : RoomDatabase() {

    abstract fun sportsPerformanceDao(): SportsPerformanceDao

    companion object {
        @Volatile
        private var INSTANCE: SportsDatabase? = null

        fun getDatabase(context: Context): SportsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SportsDatabase::class.java,
                    "sports_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}