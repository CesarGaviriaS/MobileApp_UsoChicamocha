package com.example.testusoandroidstudio_1_usochicamocha.data.remote.dto

data class LoginResponse(
    val message: String,
    val accessToken: String?,
    val refreshToken: String?,
    val userId: Int?
)