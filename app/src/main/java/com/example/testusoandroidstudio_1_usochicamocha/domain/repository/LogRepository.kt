package com.example.testusoandroidstudio_1_usochicamocha.domain.repository

import com.example.testusoandroidstudio_1_usochicamocha.domain.model.LogEntry
import kotlinx.coroutines.flow.Flow

interface LogRepository {
    fun getLogs(): Flow<List<LogEntry>>
}