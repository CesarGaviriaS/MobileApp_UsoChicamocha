package com.example.testusoandroidstudio_1_usochicamocha.util

import com.example.testusoandroidstudio_1_usochicamocha.data.local.dao.LogDao
import com.example.testusoandroidstudio_1_usochicamocha.data.local.entity.LogEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Herramienta centralizada para guardar logs en la base de datos.
 */
@Singleton
class AppLogger @Inject constructor(
    private val logDao: LogDao
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    fun log(message: String) {
        scope.launch {
            val logEntry = LogEntity(
                timestamp = System.currentTimeMillis(),
                message = message
            )
            logDao.insert(logEntry)
        }
    }
}
