package com.example.testusoandroidstudio_1_usochicamocha.domain.model

data class Machine(
    val id: Int,
    val name: String,
    val brand: String,
    val model: String,
    val engineNumber: String,
    val internalIdentificationNumber: String,
    val runtExpirationDate: String?,
    val soatExpirationDate: String?,
    var isSelected: Boolean = false
)
