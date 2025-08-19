package com.example.testusoandroidstudio_1_usochicamocha.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.testusoandroidstudio_1_usochicamocha.data.local.entity.MachineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MachineDao {
    // Inserta una lista de máquinas. Si ya existen, las reemplaza.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(machines: List<MachineEntity>)

    // Obtiene todas las máquinas guardadas, ordenadas por nombre.
    @Query("SELECT * FROM machines ORDER BY name ASC")
    fun getAllMachines(): Flow<List<MachineEntity>>

    // Cuenta cuántas máquinas hay en la tabla.
    @Query("SELECT COUNT(id) FROM machines")
    suspend fun count(): Int

    // Borra todas las filas de la tabla 'machines'.
    @Query("DELETE FROM machines")
    suspend fun deleteAll()

    // --- LÓGICA DE TRANSACCIÓN AÑADIDA ---
    // La anotación @Transaction asegura que ambas operaciones (borrar e insertar)
    // se completen exitosamente. Si una falla, ninguna se aplica.
    @Transaction
    suspend fun clearAndInsert(machines: List<MachineEntity>) {
        deleteAll()
        insertAll(machines)
    }
}