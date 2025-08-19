package com.example.testusoandroidstudio_1_usochicamocha.data.remote.dto

import com.example.testusoandroidstudio_1_usochicamocha.data.local.entity.MachineEntity
import com.google.gson.annotations.SerializedName

data class MachineDto(
    val id: Int,
    val nombre: String?,
    val modelo: String?,
    // Usamos @SerializedName para mapear el nombre de la columna de la BD
    // al nombre de la variable en Kotlin.
    @SerializedName("num_inter_identificacion")
    val identifier: String?
)

fun MachineDto.toEntity(): MachineEntity {
    return MachineEntity(
        id = id,
        name = nombre ?: "Sin Nombre",
        model = modelo ?: "N/A",
        identifier = identifier ?: "Sin ID" // <-- AÑADIDO
    )
}
