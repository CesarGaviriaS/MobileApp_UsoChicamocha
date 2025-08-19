package com.example.testusoandroidstudio_1_usochicamocha.data.remote

import com.example.testusoandroidstudio_1_usochicamocha.domain.repository.AuthRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

/**
 * Este autenticador se activa automáticamente cuando el servidor responde con un error 401.
 * Su trabajo es intentar refrescar el token de acceso y reintentar la petición original.
 */
class TokenAuthenticator @Inject constructor(
    // Usamos 'dagger.Lazy' para evitar una dependencia circular con AuthRepository.
    private val authRepository: dagger.Lazy<AuthRepository>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // Usamos runBlocking porque la interfaz de Authenticator es síncrona.
        return runBlocking {
            // Intentamos refrescar el token.
            val tokenRefreshResult = authRepository.get().refreshTokenIfNecessary()

            if (tokenRefreshResult.isSuccess) {
                // Si el refresco fue exitoso, obtenemos el nuevo token de acceso.
                val newAccessToken = tokenRefreshResult.getOrNull()
                // Reintentamos la petición que falló, pero ahora con el nuevo token.
                response.request.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()
            } else {
                // Si el refresco falla (ej. el refresh token también expiró),
                // cerramos la sesión y no reintentamos la petición.
                authRepository.get().logout()
                null
            }
        }
    }
}