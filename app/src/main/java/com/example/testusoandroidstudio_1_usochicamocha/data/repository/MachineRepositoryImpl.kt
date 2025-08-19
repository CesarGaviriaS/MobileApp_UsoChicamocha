package com.example.testusoandroidstudio_1_usochicamocha.data.repository

import android.util.Log
import com.example.testusoandroidstudio_1_usochicamocha.data.local.dao.MachineDao
import com.example.testusoandroidstudio_1_usochicamocha.data.local.entity.toDomain
import com.example.testusoandroidstudio_1_usochicamocha.data.remote.ApiService
import com.example.testusoandroidstudio_1_usochicamocha.data.remote.dto.toEntity
import com.example.testusoandroidstudio_1_usochicamocha.domain.model.Machine
import com.example.testusoandroidstudio_1_usochicamocha.domain.repository.MachineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MachineRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val machineDao: MachineDao
) : MachineRepository {

    override suspend fun syncMachines(): Result<Unit> {
        return try {
            val response = apiService.getMachines()
            if (response.isSuccessful && response.body() != null) {
                val machinesDto = response.body()!!

                if (machinesDto.isEmpty() && machineDao.count() == 0) {
                    return Result.failure(Exception("Error fatal: No se encontraron máquinas en el servidor."))
                }

                // --- ESTA ES LA LÍNEA MÁS IMPORTANTE ---
                // Convierte los datos de la red a entidades de base de datos y los inserta.
                val machineEntities = machinesDto.map { it.toEntity() }
                machineDao.clearAndInsert(machineEntities)
                // --- FIN DE LA LÍNEA IMPORTANTE ---


                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al obtener las máquinas del servidor."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getLocalMachines(): Flow<List<Machine>> {
        return machineDao.getAllMachines().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}