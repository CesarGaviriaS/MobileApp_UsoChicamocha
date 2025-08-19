package com.example.testusoandroidstudio_1_usochicamocha.util

import android.util.Log
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object JwtUtils {

    /**
     * Comprueba si un token JWT ha expirado, AHORA indicando qué tipo de token es.
     * @param token El token JWT a verificar.
     * @param tokenType Un String para identificar el token en los logs (ej. "Access Token").
     * @return `true` si el token ha expirado, `false` en caso contrario.
     */
    fun isTokenExpired(token: String, tokenType: String): Boolean {
        try {
            val parts = token.split(".")
            if (parts.size != 3) return true

            val payloadBytes = android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE)
            val payloadJson = String(payloadBytes, Charsets.UTF_8)
            val payload = JSONObject(payloadJson)

            val exp = payload.optLong("exp")
            if (exp == 0L) return true

            val expirationInMillis = exp * 1000
            val currentTimeInMillis = System.currentTimeMillis()

            val isExpired = expirationInMillis < currentTimeInMillis

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val expirationDate = sdf.format(Date(expirationInMillis))
            val currentDate = sdf.format(Date(currentTimeInMillis))

            // --- LOGS MEJORADOS ---
            Log.d("JwtUtils", "---------------------------------")
            Log.d("JwtUtils", "Verificando Expiración de: $tokenType")
            Log.d("JwtUtils", "   - Fecha de Expiración: $expirationDate")
            Log.d("JwtUtils", "   - Fecha Actual:        $currentDate")
            Log.d("JwtUtils", "   - ¿Ha expirado?: $isExpired")
            Log.d("JwtUtils", "---------------------------------")

            return isExpired
        } catch (e: Exception) {
            e.printStackTrace()
            return true
        }
    }
}