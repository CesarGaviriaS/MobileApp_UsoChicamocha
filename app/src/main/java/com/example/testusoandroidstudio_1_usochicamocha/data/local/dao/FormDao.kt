package com.example.testusoandroidstudio_1_usochicamocha.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testusoandroidstudio_1_usochicamocha.data.local.entity.FormEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FormDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForm(form: FormEntity)
    @Query("SELECT * FROM pending_forms WHERE isSynced = 0 ORDER BY timestamp DESC")
    fun getPendingForms(): Flow<List<FormEntity>>

    @Query("UPDATE pending_forms SET isSynced = 1 WHERE uuid = :uuid")
    suspend fun markAsSynced(uuid: String)
}
