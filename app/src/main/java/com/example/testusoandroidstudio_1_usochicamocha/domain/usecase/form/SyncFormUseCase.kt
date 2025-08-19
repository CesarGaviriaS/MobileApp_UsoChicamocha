package com.example.testusoandroidstudio_1_usochicamocha.domain.usecase.form

import com.example.testusoandroidstudio_1_usochicamocha.domain.model.Form
import com.example.testusoandroidstudio_1_usochicamocha.domain.repository.FormRepository
import javax.inject.Inject


class SyncFormUseCase @Inject constructor(
    private val repository: FormRepository
) {
    suspend operator fun invoke(form: Form): Result<Unit> {
        return repository.syncForm(form)
    }
}
