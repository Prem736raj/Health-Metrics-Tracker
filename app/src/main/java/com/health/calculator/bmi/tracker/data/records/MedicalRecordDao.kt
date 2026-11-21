package com.health.calculator.bmi.tracker.data.records

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
internal interface MedicalRecordDao {
    @Query("SELECT * FROM medical_records ORDER BY timestamp DESC, id")
    fun observe(): Flow<List<MedicalRecord>>

    @Query("SELECT * FROM medical_records WHERE timestamp BETWEEN :start AND :end ORDER BY timestamp DESC, id")
    suspend fun inRange(start: Long, end: Long): List<MedicalRecord>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(record: MedicalRecord): Long

    @Update
    suspend fun update(record: MedicalRecord): Int

    @Query("DELETE FROM medical_records WHERE id = :id")
    suspend fun delete(id: String)
}
