package com.example.testusoandroidstudio_1_usochicamocha.domain.usecase.auth

import com.example.testusoandroidstudio_1_usochicamocha.domain.model.UserSession
import com.example.testusoandroidstudio_1_usochicamocha.domain.repository.AuthRepository
import com.example.testusoandroidstudio_1_usochicamocha.util.AppLogger
import javax.inject.Inject

/**
 * Caso de Uso para el inicio de sesión. Orquesta la lógica del flujo de login.
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val logger: AppLogger // <-- Inyectamos el logger
) {
    suspend operator fun invoke(user: String, pass: String): Result<UserSession> {
        logger.log("Intento de login para usuario '$user'")
        val result = authRepository.login(user, pass)
        if (result.isSuccess) {
            logger.log("Login exitoso para '$user'")
        } else {
            logger.log("Login fallido para '$user': ${result.exceptionOrNull()?.message}")
        }
        return result
    }
}