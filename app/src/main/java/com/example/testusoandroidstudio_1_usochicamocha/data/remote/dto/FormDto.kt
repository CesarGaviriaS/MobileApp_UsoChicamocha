package com.example.testusoandroidstudio_1_usochicamocha.data.remote.dto

import com.squareup.moshi.Json

// Esta data class representa el JSON que enviaremos al servidor.
// Los nombres de las propiedades deben coincidir con lo que el backend espera.
data class FormDto(
    @field:Json(name = "uuid")
    val uuid: String,

    @field:Json(name = "date_stamp")
    val dateStamp: String,

    // --- CAMBIO AQUÍ ---
    // Ajustado para reflejar la nueva estructura de la tabla 'usuarios'.
    @field:Json(name = "usuario_id")
    val usuarioId: Int,

    @field:Json(name = "equipos_id")
    val equipoId: Int,

    @field:Json(name = "hourmeter")
    val horometro: Double,

    @field:Json(name = "estado_fugas")
    val estadoFugas: String,

    @field:Json(name = "estado_frenos")
    val estadoFrenos: String,

    @field:Json(name = "estado_correas_poleas")
    val estadoCorreasPoleas: String,

    @field:Json(name = "estado_llantas_carriles")
    val estadoLlantasCarriles: String,

    @field:Json(name = "estado_encendido")
    val estadoEncendido: String,

    @field:Json(name = "estado_electrico")
    val estadoElectrico: String,

    @field:Json(name = "estado_mecanico")
    val estadoMecanico: String,

    @field:Json(name = "estado_temperatura")
    val estadoTemperatura: String,

    @field:Json(name = "estado_aceite")
    val estadoAceite: String,

    @field:Json(name = "estado_hidraulico")
    val estadoHidraulico: String,

    @field:Json(name = "estado_refrigerante")
    val estadoRefrigerante: String,

    @field:Json(name = "estado_estructural")
    val estadoEstructural: String,

    @field:Json(name = "vigencia_extintor")
    val vigenciaExtintor: String,

    @field:Json(name = "observaciones")
    val observaciones: String
)
