package com.example.sports.data.remote.dto

import com.example.sports.domain.model.SportsPerformance
import com.example.sports.domain.model.StorageType
import java.util.Date

data class SportsPerformanceDto(
    val id: String? = null,
    val name: String = "",
    val location: String = "",
    val duration: Int = 0,
    val storageType: String = "REMOTE",
    val createdAt: Long = System.currentTimeMillis()
)

fun SportsPerformanceDto.toDomain(): SportsPerformance {
    return SportsPerformance(
        id = id?.hashCode()?.toLong() ?: 0L, // Convert string ID to long for compatibility
        name = name,
        location = location,
        duration = duration,
        storageType = try {
            StorageType.valueOf(storageType)
        } catch (e: Exception) {
            StorageType.REMOTE
        },
        createdAt = Date(createdAt)
    )
}

fun SportsPerformance.toFirebaseDto(): SportsPerformanceDto {
    return SportsPerformanceDto(
        id = if (id == 0L) null else id.toString(),
        name = name,
        location = location,
        duration = duration,
        storageType = storageType.name,
        createdAt = createdAt.time
    )
}