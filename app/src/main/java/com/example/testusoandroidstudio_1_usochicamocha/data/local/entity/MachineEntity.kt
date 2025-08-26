package com.example.testusoandroidstudio_1_usochicamocha.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testusoandroidstudio_1_usochicamocha.domain.model.Machine

@Entity(tableName = "machines")
data class MachineEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val brand: String,
    val model: String,
    val engineNumber: String,
    val internalIdentificationNumber: String,
    val runtExpirationDate: String?,
    val soatExpirationDate: String?
)

fun MachineEntity.toDomain(): Machine {
    return Machine(
        id = id,
        name = name,
        brand = brand,
        model = model,
        engineNumber = engineNumber,
        internalIdentificationNumber = internalIdentificationNumber,
        runtExpirationDate = runtExpirationDate,
        soatExpirationDate = soatExpirationDate,
        // isSelected se manejará en el dominio/ViewModel, no es parte de la entidad persistida directamente desde el DTO
    )
}
