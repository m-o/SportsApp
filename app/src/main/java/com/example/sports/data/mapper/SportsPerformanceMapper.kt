package com.example.sports.data.mapper

import com.example.sports.data.local.entity.SportsPerformanceEntity
import com.example.sports.data.remote.dto.SportsPerformanceDto
import com.example.sports.domain.model.SportsPerformance
import com.example.sports.domain.model.StorageType
import java.util.Date

object SportsPerformanceMapper {

    fun fromEntityToDomain(entity: SportsPerformanceEntity): SportsPerformance {
        return SportsPerformance(
            id = entity.id,
            name = entity.name,
            location = entity.location,
            duration = entity.duration,
            storageType = StorageType.valueOf(entity.storageType),
            createdAt = Date(entity.createdAt)
        )
    }

    fun fromDomainToEntity(domain: SportsPerformance): SportsPerformanceEntity {
        return SportsPerformanceEntity(
            id = domain.id,
            name = domain.name,
            location = domain.location,
            duration = domain.duration,
            storageType = domain.storageType.name,
            createdAt = domain.createdAt.time
        )
    }

    fun fromDtoToDomain(dto: SportsPerformanceDto): SportsPerformance {
        return SportsPerformance(
            id = dto.id?.toLongOrNull() ?: 0,
            name = dto.name,
            location = dto.location,
            duration = dto.duration,
            storageType = StorageType.valueOf(dto.storageType),
            createdAt = Date(dto.createdAt)
        )
    }

    fun fromDomainToDto(domain: SportsPerformance): SportsPerformanceDto {
        return SportsPerformanceDto(
            id = if (domain.id != 0L) domain.id.toString() else null,
            name = domain.name,
            location = domain.location,
            duration = domain.duration,
            storageType = domain.storageType.name,
            createdAt = domain.createdAt.time
        )
    }
}