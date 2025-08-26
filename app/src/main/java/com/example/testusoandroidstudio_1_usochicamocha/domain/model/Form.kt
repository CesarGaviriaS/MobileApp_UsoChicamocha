package com.example.testusoandroidstudio_1_usochicamocha.domain.model

import com.example.testusoandroidstudio_1_usochicamocha.data.local.entity.FormEntity

data class Form(
    val localId: Int,
    val UUID: String,
    val timestamp: Long,
    val machineId: Long, // Corregido de equipoId: Int a machineId: Long
    val userId: Long,    // Corregido de usuarioId: Int a userId: Long
    val hourmeter: String, // Corregido de horometro: Double a hourmeter: String
    val leakStatus: String,
    val brakeStatus: String,
    val beltsPulleysStatus: String,
    val tireLanesStatus: String,
    val carIgnitionStatus: String,
    val electricalStatus: String,
    val mechanicalStatus: String,
    val temperatureStatus: String,
    val oilStatus: String,
    val hydraulicStatus: String,
    val coolantStatus: String,
    val structuralStatus: String,
    val expirationDateFireExtinguisher: String,
    val observations: String,
    val isSynced: Boolean
)

fun Form.toEntity(): FormEntity {
    return FormEntity(
        localId = this.localId,
        UUID = this.UUID,
        timestamp = this.timestamp,
        machineId = this.machineId, // Corregido de equipoId
        userId = this.userId,       // Corregido de usuarioId
        hourmeter = this.hourmeter, // Corregido de horometro
        leakStatus = this.leakStatus,
        brakeStatus = this.brakeStatus,
        beltsPulleysStatus = this.beltsPulleysStatus,
        tireLanesStatus = this.tireLanesStatus,
        carIgnitionStatus = this.carIgnitionStatus,
        electricalStatus = this.electricalStatus,
        mechanicalStatus = this.mechanicalStatus,
        temperatureStatus = this.temperatureStatus,
        oilStatus = this.oilStatus,
        hydraulicStatus = this.hydraulicStatus,
        coolantStatus = this.coolantStatus,
        structuralStatus = this.structuralStatus,
        expirationDateFireExtinguisher = this.expirationDateFireExtinguisher,
        observations = this.observations,
        isSynced = this.isSynced
    )
}
