package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DialysisDao {

    // Patient Profile
    @Query("SELECT * FROM patient_profile WHERE id = 1")
    fun getPatientProfile(): Flow<PatientProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: PatientProfileEntity)

    // Medical Info
    @Query("SELECT * FROM medical_info WHERE id = 1")
    fun getMedicalInfo(): Flow<MedicalInfoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateMedicalInfo(info: MedicalInfoEntity)

    // Dialysis Passport
    @Query("SELECT * FROM dialysis_passport WHERE id = 1")
    fun getDialysisPassport(): Flow<DialysisPassportEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePassport(passport: DialysisPassportEntity)

    // Sessions
    @Query("SELECT * FROM dialysis_sessions ORDER BY dateTimestamp DESC")
    fun getAllSessions(): Flow<List<DialysisSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: DialysisSessionEntity)

    @Delete
    suspend fun deleteSession(session: DialysisSessionEntity)

    // Home Monitoring
    @Query("SELECT * FROM home_monitoring ORDER BY timestamp DESC")
    fun getAllHomeMonitoring(): Flow<List<HomeMonitoringEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHomeMonitoring(entry: HomeMonitoringEntity)

    // Lab Results
    @Query("SELECT * FROM lab_results ORDER BY dateTimestamp DESC")
    fun getAllLabResults(): Flow<List<LabResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLabResult(lab: LabResultEntity)

    // Medications
    @Query("SELECT * FROM medications WHERE isHistory = 0 ORDER BY id DESC")
    fun getActiveMedications(): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medications WHERE isHistory = 1 ORDER BY id DESC")
    fun getMedicationHistory(): Flow<List<MedicationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medication: MedicationEntity)

    @Update
    suspend fun updateMedication(medication: MedicationEntity)

    // Nutrition
    @Query("SELECT * FROM nutrition_log ORDER BY timestamp DESC")
    fun getAllNutritionEntries(): Flow<List<NutritionEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNutritionEntry(entry: NutritionEntryEntity)

    // Documents
    @Query("SELECT * FROM medical_documents ORDER BY id DESC")
    fun getAllDocuments(): Flow<List<MedicalDocumentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(doc: MedicalDocumentEntity)
}
