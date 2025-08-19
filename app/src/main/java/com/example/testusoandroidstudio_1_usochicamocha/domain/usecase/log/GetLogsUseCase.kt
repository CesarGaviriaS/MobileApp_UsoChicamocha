package com.example.testusoandroidstudio_1_usochicamocha.domain.usecase.log

import com.example.testusoandroidstudio_1_usochicamocha.domain.model.LogEntry
import com.example.testusoandroidstudio_1_usochicamocha.domain.repository.LogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLogsUseCase @Inject constructor(
    private val logRepository: LogRepository
) {
    operator fun invoke(): Flow<List<LogEntry>> {
        return logRepository.getLogs()
    }
}