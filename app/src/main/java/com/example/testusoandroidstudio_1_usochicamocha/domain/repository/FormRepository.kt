package com.example.testusoandroidstudio_1_usochicamocha.domain.repository

import com.example.testusoandroidstudio_1_usochicamocha.domain.model.Form
import kotlinx.coroutines.flow.Flow

// Aún no hemos definido este repositorio, así que lo creamos ahora.
interface FormRepository {
    fun getPendingForms(): Flow<List<Form>>
    suspend fun saveFormLocally(form: Form)
    suspend fun syncForm(form: Form): Result<Unit>
   }