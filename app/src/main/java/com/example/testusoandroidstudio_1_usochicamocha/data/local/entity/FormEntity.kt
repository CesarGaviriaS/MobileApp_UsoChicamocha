package com.example.testusoandroidstudio_1_usochicamocha.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testusoandroidstudio_1_usochicamocha.domain.model.Form

@Entity(tableName = "pending_forms")
data class FormEntity(
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
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
    val observaciones: String,
    var isSynced: Boolean = false
)

// --- FUNCIÓN DE MAPEO AÑADIDA ---
// ... (código de la data class FormEntity) ...

fun FormEntity.toDomain(): Form {
    return Form(
        localId = localId,
        uuid = this.uuid,
        timestamp = timestamp,
        equipoId = equipoId,
        usuarioId = usuarioId,
        horometro = horometro,
        estadoFugas = estadoFugas,
        estadoFrenos = estadoFrenos,
        estadoCorreasPoleas = estadoCorreasPoleas,
        estadoLlantasCarriles = estadoLlantasCarriles,
        estadoEncendido = estadoEncendido,
        estadoElectrico = estadoElectrico,
        estadoMecanico = estadoMecanico,
        estadoTemperatura = estadoTemperatura,
        estadoAceite = estadoAceite,
        estadoHidraulico = estadoHidraulico,
        estadoRefrigerante = estadoRefrigerante,
        estadoEstructural = estadoEstructural,
        vigenciaExtintor = vigenciaExtintor,
        observaciones = observaciones
    )
}