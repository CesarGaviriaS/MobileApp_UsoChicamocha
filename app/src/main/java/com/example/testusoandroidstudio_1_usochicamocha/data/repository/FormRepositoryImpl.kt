package com.example.testusoandroidstudio_1_usochicamocha.data.repository

import com.example.testusoandroidstudio_1_usochicamocha.data.local.dao.FormDao
import com.example.testusoandroidstudio_1_usochicamocha.data.local.entity.toDomain
import com.example.testusoandroidstudio_1_usochicamocha.data.remote.ApiService
import com.example.testusoandroidstudio_1_usochicamocha.data.remote.dto.FormDto
import com.example.testusoandroidstudio_1_usochicamocha.domain.model.Form
import com.example.testusoandroidstudio_1_usochicamocha.domain.model.toEntity
import com.example.testusoandroidstudio_1_usochicamocha.domain.repository.FormRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject


class FormRepositoryImpl @Inject constructor(
    private val formDao: FormDao,
    private val apiService: ApiService

) : FormRepository {

    // --- IMPLEMENTACIÓN DEL MÉTODO ---
    override fun getPendingForms(): Flow<List<Form>> {
        return formDao.getPendingForms().map { entities ->
            entities.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun saveFormLocally(form: Form) {
        val formEntity = form.toEntity()
        formDao.insertForm(formEntity)
    }

    override suspend fun syncForm(form: Form): Result<Unit> {
        return try {
            // 1. Convertimos el objeto de dominio a un DTO para la red.
            val formDto = form.toDto()

            // 2. Hacemos la llamada a la API.
            val response = apiService.syncForm(formDto)

            if (response.isSuccessful) {
                // 3. Si la API responde OK, marcamos el formulario como sincronizado en la BD local.
                formDao.markAsSynced(form.uuid)
                Result.success(Unit)
            } else {
                // Si la API responde con un error (4xx, 5xx), devolvemos un fallo.
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            // Si ocurre cualquier otra excepción (ej. no hay internet), la capturamos.
            Result.failure(e)
        }
    }

    private fun Form.toDto(): FormDto {
        // El servidor espera la fecha en formato ISO 8601 (UTC).
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("America/Bogota") // <-- La línea clave
        val isoDateString = sdf.format(Date(this.timestamp))

        return FormDto(
            uuid = this.uuid,
            dateStamp = isoDateString,
            usuarioId = this.usuarioId,
            equipoId = this.equipoId,
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
            observaciones = this.observaciones
        )
    }
}