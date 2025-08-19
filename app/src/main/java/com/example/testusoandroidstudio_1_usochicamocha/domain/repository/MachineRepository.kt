package com.example.testusoandroidstudio_1_usochicamocha.domain.repository

import com.example.testusoandroidstudio_1_usochicamocha.domain.model.Machine
import kotlinx.coroutines.flow.Flow

interface MachineRepository {
    suspend fun syncMachines(): Result<Unit>
    fun getLocalMachines(): Flow<List<Machine>>
}