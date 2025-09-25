package com.example.sports.data.repository

import com.example.sports.data.local.dao.SportsPerformanceDao
import com.example.sports.data.mapper.SportsPerformanceMapper
import com.example.sports.data.remote.datasource.FirebaseSportsPerformanceDataSource
import com.example.sports.data.remote.dto.toDomain
import com.example.sports.data.remote.dto.toFirebaseDto
import com.example.sports.domain.model.SportsPerformance
import com.example.sports.domain.model.StorageType
import com.example.sports.domain.repository.SportsPerformanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class SportsPerformanceRepositoryImpl(
    private val localDao: SportsPerformanceDao,
    private val firebaseDataSource: FirebaseSportsPerformanceDataSource
) : SportsPerformanceRepository {

    override fun getAllPerformances(): Flow<List<SportsPerformance>> {
        val localFlow = localDao.getAllPerformances().map { entities ->
            entities.map { SportsPerformanceMapper.fromEntityToDomain(it) }
        }

        val firebaseFlow = firebaseDataSource.getAllPerformances().map { dtos ->
            dtos.map { it.toDomain() }
        }

        return combine(localFlow, firebaseFlow) { localPerformances, firebasePerformances ->
            (localPerformances + firebasePerformances).sortedByDescending { it.createdAt }
        }
    }

    override fun getPerformancesByType(storageType: StorageType): Flow<List<SportsPerformance>> {
        return when (storageType) {
            StorageType.LOCAL -> localDao.getPerformancesByType(storageType.name).map { entities ->
                entities.map { SportsPerformanceMapper.fromEntityToDomain(it) }
            }
            StorageType.REMOTE -> firebaseDataSource.getAllPerformances().map { dtos ->
                dtos.map { it.toDomain() }
            }
        }
    }

    override suspend fun getPerformanceById(id: Long): SportsPerformance? {
        return localDao.getPerformanceById(id)?.let {
            SportsPerformanceMapper.fromEntityToDomain(it)
        }
    }

    override suspend fun insertPerformance(performance: SportsPerformance): Long {
        return when (performance.storageType) {
            StorageType.LOCAL -> {
                val entity = SportsPerformanceMapper.fromDomainToEntity(performance)
                localDao.insertPerformance(entity)
            }
            StorageType.REMOTE -> {
                val dto = performance.toFirebaseDto()
                firebaseDataSource.insertPerformance(dto)
                performance.id // Return the existing ID or generate one
            }
        }
    }

    override suspend fun updatePerformance(performance: SportsPerformance) {
        val entity = SportsPerformanceMapper.fromDomainToEntity(performance)
        localDao.updatePerformance(entity)
    }

    override suspend fun deletePerformance(performance: SportsPerformance) {
        when (performance.storageType) {
            StorageType.LOCAL -> {
                val entity = SportsPerformanceMapper.fromDomainToEntity(performance)
                localDao.deletePerformance(entity)
            }
            StorageType.REMOTE -> {
                val firebaseId = performance.id.toString()
                firebaseDataSource.deletePerformance(firebaseId)
            }
        }
    }

    override suspend fun deleteAllPerformances() {
        localDao.deleteAllPerformances()
        firebaseDataSource.deleteAllPerformances()
    }
}