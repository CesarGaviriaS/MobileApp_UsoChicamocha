package com.example.testusoandroidstudio_1_usochicamocha.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Delega la creación del DataStore al contexto de la aplicación.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session_data")

/**
 * Gestiona el almacenamiento y la recuperación de los datos de sesión (tokens y userId)
 * de forma segura usando Jetpack DataStore.
 * Integrado con Hilt para ser un Singleton en toda la app.
 */
@Singleton // 1. Hacemos que sea una instancia única para toda la app.
class TokenManager @Inject constructor(@ApplicationContext private val context: Context) { // 2. Hilt se encarga de proveer el contexto.

    companion object {
        // --- CLAVES PARA DATOS DE SESIÓN ---
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        val USER_ID_KEY = intPreferencesKey("user_id") // 3. Creamos la clave para el ID del usuario.
    }

    // --- MÉTODOS PARA GUARDAR ---

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    // 4. Creamos la función para guardar el ID del usuario.
    suspend fun saveUserId(id: Int) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = id
        }
    }

    // --- MÉTODOS PARA LEER ---

    fun getAccessToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_KEY]
        }
    }

    fun getRefreshToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN_KEY]
        }
    }

    // 5. Creamos la función para leer el ID del usuario. Devuelve un Flow.
    fun getUserId(): Flow<Int?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ID_KEY]
        }
    }

    // --- MÉTODO PARA BORRAR ---

    suspend fun clearSessionData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
