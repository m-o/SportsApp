package com.example.sports.domain.model

import java.util.Date

//could do 2 types of domain object(different id), like sealed class, local/remote, or one with enum
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