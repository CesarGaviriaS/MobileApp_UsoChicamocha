package com.example.testusoandroidstudio_1_usochicamocha.domain.model


data class Inspection(
    val id: Long,
    val UUID: String?,
    val beltsPulleysStatus: String?,
    val brakeStatus: String?,
    val carIgnitionStatus: String?,
    val coolantStatus: String?,
    val dateStamp: String?,
    val electricalStatus: String?,
    val expirationDateFireExtinguisher: String?,
    val hourmeter: String?,
    val hydraulicStatus: String?,
    val leakStatus: String?,
    val mechanicalStatus: String?,
    val observations: String?,
    val oilStatus: String?,
    val structuralStatus: String?,
    val temperatureStatus: String?,
    val tireLanesStatus: String?,
    val machineId: Long,
    val userId: Long
)
