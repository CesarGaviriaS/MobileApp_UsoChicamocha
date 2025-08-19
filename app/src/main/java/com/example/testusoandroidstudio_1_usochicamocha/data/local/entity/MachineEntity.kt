package com.example.testusoandroidstudio_1_usochicamocha.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testusoandroidstudio_1_usochicamocha.domain.model.Machine

@Entity(tableName = "machines")
data class MachineEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val model: String,
    val identifier: String // <-- CAMPO AÑADIDO
)

fun MachineEntity.toDomain(): Machine {
    return Machine(
        id = id,
        name = name,
        model = model,
        identifier = identifier // <-- AÑADIDO
    )
}