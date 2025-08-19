package com.example.testusoandroidstudio_1_usochicamocha.domain.usecase.form

import com.example.testusoandroidstudio_1_usochicamocha.domain.model.Form
import com.example.testusoandroidstudio_1_usochicamocha.domain.repository.FormRepository
import javax.inject.Inject

class SaveFormUseCase @Inject constructor(

    private val formRepository: FormRepository

) {

    suspend operator fun invoke(form: Form) {

        formRepository.saveFormLocally(form)

    }

}