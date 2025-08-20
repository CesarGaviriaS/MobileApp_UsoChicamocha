package com.example.testusoandroidstudio_1_usochicamocha.data.remote.dto

import com.example.testusoandroidstudio_1_usochicamocha.data.local.entity.MachineEntity
import com.google.gson.annotations.SerializedName

data class MachineDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val nombre: String?,
    @SerializedName("brand")
    val modelo: String?,
    @SerializedName("numInterIdentification")
    val identifier: String?
)

fun MachineDto.toEntity(): MachineEntity {
    return MachineEntity(
        id = id,
        name = nombre ?: "Sin Nombre",
        model = modelo ?: "N/A",
        identifier = identifier ?: "Sin ID"
    )
}
