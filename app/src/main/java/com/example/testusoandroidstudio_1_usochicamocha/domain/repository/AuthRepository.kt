package com.example.testusoandroidstudio_1_usochicamocha.domain.repository

import com.example.testusoandroidstudio_1_usochicamocha.domain.model.UserSession

interface AuthRepository {
    suspend fun login(user: String, pass: String): Result<UserSession>
    suspend fun refreshTokenIfNecessary(): Result<String>
    suspend fun logout()
    }
