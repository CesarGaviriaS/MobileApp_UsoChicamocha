package com.example.testusoandroidstudio_1_usochicamocha.domain.model

import com.example.testusoandroidstudio_1_usochicamocha.data.local.entity.FormEntity


data class Form(
    val localId: Int,
    val uuid: String,
    val timestamp: Long,
    val equipoId: Int,
    val usuarioId: Int,
    val horometro: Double,
    val estadoFugas: String,
    val estadoFrenos: String,
    val estadoCorreasPoleas: String,
    val estadoLlantasCarriles: String,
    val estadoEncendido: String,
    val estadoElectrico: String,
    val estadoMecanico: String,
    val estadoTemperatura: String,
    val estadoAceite: String,
    val estadoHidraulico: String,
    val estadoRefrigerante: String,
    val estadoEstructural: String,
    val vigenciaExtintor: String,
    val observaciones: String
)

// --- FUNCIÓN QUE DEBES AÑADIR ---
fun Form.toEntity(): FormEntity {
    return FormEntity(
        localId = this.localId,
        uuid = this.uuid,
        timestamp = this.timestamp,
        equipoId = this.equipoId,
        usuarioId = this.usuarioId,
        horometro = this.horometro,
        estadoFugas = this.estadoFugas,
        estadoFrenos = this.estadoFrenos,
        estadoCorreasPoleas = this.estadoCorreasPoleas,
        estadoLlantasCarriles = this.estadoLlantasCarriles,
        estadoEncendido = this.estadoEncendido,
        estadoElectrico = this.estadoElectrico,
        estadoMecanico = this.estadoMecanico,
        estadoTemperatura = this.estadoTemperatura,
        estadoAceite = this.estadoAceite,
        estadoHidraulico = this.estadoHidraulico,
        estadoRefrigerante = this.estadoRefrigerante,
        estadoEstructural = this.estadoEstructural,
        vigenciaExtintor = this.vigenciaExtintor,
        observaciones = this.observaciones,
        isSynced = false // Al guardar localmente, siempre se marca como no sincronizado
    )
}