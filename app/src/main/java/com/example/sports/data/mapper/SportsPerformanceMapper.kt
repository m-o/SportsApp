package com.example.sports.data.mapper

import com.example.sports.data.local.entity.SportsPerformanceEntity
import com.example.sports.data.remote.dto.SportsPerformanceDto
import com.example.sports.domain.model.SportsPerformance
import com.example.sports.domain.model.StorageType
import java.util.Date

object SportsPerformanceMapper {

    fun fromEntityToDomain(entity: SportsPerformanceEntity): SportsPerformance {
        return SportsPerformance(
            localId = entity.id,
            firebaseId = null, // Local items don't have Firebase IDs
            name = entity.name,
            location = entity.location,
            duration = entity.duration,
            storageType = StorageType.valueOf(entity.storageType),
            createdAt = Date(entity.createdAt)
        )
    }

    fun fromDomainToEntity(domain: SportsPerformance): SportsPerformanceEntity {
        return SportsPerformanceEntity(
            id = domain.localId,
            name = domain.name,
            location = domain.location,
            duration = domain.duration,
            storageType = domain.storageType.name,
            createdAt = domain.createdAt.time
        )
    }

}