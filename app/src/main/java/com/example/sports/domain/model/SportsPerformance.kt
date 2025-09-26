package com.example.sports.domain.model

import java.util.Date

data class SportsPerformance(
    val localId: Long = 0,
    val firebaseId: String? = null,
    val name: String,
    val location: String,
    val duration: Int,
    val storageType: StorageType,
    val createdAt: Date = Date()
)

enum class StorageType(val displayName: String) {
    LOCAL("Local"),
    REMOTE("Remote")
}