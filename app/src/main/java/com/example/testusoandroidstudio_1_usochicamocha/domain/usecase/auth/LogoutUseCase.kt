package com.example.testusoandroidstudio_1_usochicamocha.domain.usecase.auth

import com.example.testusoandroidstudio_1_usochicamocha.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Caso de Uso para cerrar la sesión del usuario.
 * Su única responsabilidad es llamar al repositorio para que borre los tokens.
 */
class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.logout()
    }
}
