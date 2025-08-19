package com.example.testusoandroidstudio_1_usochicamocha.domain.usecase.machine

import com.example.testusoandroidstudio_1_usochicamocha.domain.model.Machine
import com.example.testusoandroidstudio_1_usochicamocha.domain.repository.MachineRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocalMachinesUseCase @Inject constructor(
    private val machineRepository: MachineRepository
) {
    operator fun invoke(): Flow<List<Machine>> {
        return machineRepository.getLocalMachines()
    }
}
