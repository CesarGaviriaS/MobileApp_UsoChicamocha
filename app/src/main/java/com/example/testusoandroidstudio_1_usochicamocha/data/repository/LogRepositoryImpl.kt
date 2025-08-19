package com.example.testusoandroidstudio_1_usochicamocha.data.repository

import com.example.testusoandroidstudio_1_usochicamocha.data.local.dao.LogDao
import com.example.testusoandroidstudio_1_usochicamocha.data.local.entity.toDomain
import com.example.testusoandroidstudio_1_usochicamocha.domain.model.LogEntry
import com.example.testusoandroidstudio_1_usochicamocha.domain.repository.LogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LogRepositoryImpl @Inject constructor(
    private val logDao: LogDao
) : LogRepository {
    override fun getLogs(): Flow<List<LogEntry>> {
        return logDao.getAllLogs().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
