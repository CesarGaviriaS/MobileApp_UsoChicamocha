package com.example.testusoandroidstudio_1_usochicamocha.domain.usecase.form

import com.example.testusoandroidstudio_1_usochicamocha.domain.model.Form
import com.example.testusoandroidstudio_1_usochicamocha.domain.repository.FormRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de Uso para obtener la lista de formularios pendientes de sincronizar.
 */
class GetPendingFormsUseCase @Inject constructor(
    private val formRepository: FormRepository
) {
    operator fun invoke(): Flow<List<Form>> {
        return formRepository.getPendingForms()
    }
}
