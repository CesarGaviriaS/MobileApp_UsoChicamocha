package com.example.testusoandroidstudio_1_usochicamocha.domain.usecase.machine

import com.example.testusoandroidstudio_1_usochicamocha.domain.repository.MachineRepository
import com.example.testusoandroidstudio_1_usochicamocha.util.AppLogger
import javax.inject.Inject

class SyncMachinesUseCase @Inject constructor(
    private val machineRepository: MachineRepository,
    private val logger: AppLogger // <-- Inyectamos el logger
) {
    suspend operator fun invoke(): Result<Unit> {
        logger.log("Intento de sincronizar máquinas...")
        val result = machineRepository.syncMachines()
        if (result.isSuccess) {
            logger.log("Sincronización de máquinas exitosa.")
        } else {
            logger.log("Sincronización de máquinas fallida: ${result.exceptionOrNull()?.message}")
        }
        return result
    }
}