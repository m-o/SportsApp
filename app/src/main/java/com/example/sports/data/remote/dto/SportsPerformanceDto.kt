package com.example.sports.data.remote.dto

import com.example.sports.domain.model.SportsPerformance
import com.example.sports.domain.model.StorageType
import java.util.Date

data class SportsPerformanceDto(
    val id: String? = null,
    val name: String = "",
    val location: String = "",
    val duration: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

fun SportsPerformanceDto.toDomain(): SportsPerformance {
    return SportsPerformance(
        localId = 0,
        firebaseId = id,
        name = name,
        location = location,
        duration = duration,
        storageType = StorageType.REMOTE,
        createdAt = Date(createdAt)
    )
}

fun SportsPerformance.toFirebaseDto(): SportsPerformanceDto {
    return SportsPerformanceDto(
        id = firebaseId,
        name = name,
        location = location,
        duration = duration,
        createdAt = createdAt.time
    )
}