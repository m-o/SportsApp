package com.example.sports.domain.model

import java.util.Date

data class SportsPerformance(
    val localId: Long = 0, // For local database items
    val firebaseId: String? = null, // For Firebase items
    val name: String,
    val location: String,
    val duration: Int, // in minutes
    val storageType: StorageType,
    val createdAt: Date = Date()
)

enum class StorageType(val displayName: String) {
    LOCAL("Local"),
    REMOTE("Remote")
}