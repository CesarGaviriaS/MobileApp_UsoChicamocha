package com.example.testusoandroidstudio_1_usochicamocha.data.repository

import com.example.testusoandroidstudio_1_usochicamocha.data.local.TokenManager
import com.example.testusoandroidstudio_1_usochicamocha.data.remote.ApiService
import com.example.testusoandroidstudio_1_usochicamocha.data.remote.dto.LoginRequest
import com.example.testusoandroidstudio_1_usochicamocha.data.remote.dto.RefreshTokenRequest
import com.example.testusoandroidstudio_1_usochicamocha.domain.model.UserSession
import com.example.testusoandroidstudio_1_usochicamocha.domain.repository.AuthRepository
import com.example.testusoandroidstudio_1_usochicamocha.util.JwtUtils
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(user: String, pass: String): Result<UserSession> {
        return try {
            val request = LoginRequest(user = user, pass = pass)
            val response = apiService.login(request)

            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!

                // --- CORRECCIÓN ---
                // Verificamos que la respuesta contenga los tokens Y el ID del usuario directamente.
                // Asegúrate de que tu LoginResponse.kt tenga una propiedad como 'val userId: Int?'.
                if (loginResponse.accessToken != null && loginResponse.refreshToken != null && loginResponse.userId != null) {
                    val accessToken = loginResponse.accessToken
                    val refreshToken = loginResponse.refreshToken
                    val userId = loginResponse.userId // <-- Obtenemos el ID directamente.

                    // --- LA LÓGICA DE GUARDADO COMPLETA ---
                    tokenManager.saveTokens(accessToken, refreshToken)
                    tokenManager.saveUserId(userId)

                    Result.success(UserSession(accessToken = accessToken, refreshToken = refreshToken))
                } else {
                    // Si falta alguna parte de la información esperada.
                    Result.failure(Exception(loginResponse.message ?: "Respuesta inválida del servidor."))
                }
            } else {
                Result.failure(Exception("Usuario o contraseña incorrectos"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("No se pudo conectar al servidor."))
        }
    }
    override suspend fun logout() {
        tokenManager.clearSessionData()
    }

    override suspend fun refreshTokenIfNecessary(): Result<String> {
        val refreshToken = tokenManager.getRefreshToken().first()
        if (refreshToken == null || JwtUtils.isTokenExpired(refreshToken, "Refresh Token")) {
            logout()
            return Result.failure(Exception("Sesión expirada."))
        }
        return try {
            val request = RefreshTokenRequest(refreshToken = refreshToken)
            val response = apiService.refreshToken(request)
            if (response.isSuccessful && response.body() != null) {
                val newAccessToken = response.body()!!.accessToken
                tokenManager.saveTokens(accessToken = newAccessToken, refreshToken = refreshToken)
                Result.success(newAccessToken)
            } else {
                logout()
                Result.failure(Exception("Refresh token inválido."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red al refrescar token: ${e.message}"))
        }
    }
}
