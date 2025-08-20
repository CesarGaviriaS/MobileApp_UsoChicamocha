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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(machines: List<MachineEntity>)

    @Query("SELECT * FROM machines ORDER BY name ASC")
    fun getAllMachines(): Flow<List<MachineEntity>>

    @Query("SELECT COUNT(id) FROM machines")
    suspend fun count(): Int

    @Query("DELETE FROM machines")
    suspend fun deleteAll()

    @Transaction
    suspend fun clearAndInsert(machines: List<MachineEntity>) {
        deleteAll()
        insertAll(machines)
    }
}