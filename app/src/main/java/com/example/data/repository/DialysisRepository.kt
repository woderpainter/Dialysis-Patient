package com.example.data.repository

import com.example.data.local.DialysisDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

class DialysisRepository(private val dao: DialysisDao) {

    val patientProfile: Flow<PatientProfileEntity?> = dao.getPatientProfile()
    val medicalInfo: Flow<MedicalInfoEntity?> = dao.getMedicalInfo()
    val dialysisPassport: Flow<DialysisPassportEntity?> = dao.getDialysisPassport()
    val sessions: Flow<List<DialysisSessionEntity>> = dao.getAllSessions()
    val homeMonitoring: Flow<List<HomeMonitoringEntity>> = dao.getAllHomeMonitoring()
    val labResults: Flow<List<LabResultEntity>> = dao.getAllLabResults()
    val activeMedications: Flow<List<MedicationEntity>> = dao.getActiveMedications()
    val medicationHistory: Flow<List<MedicationEntity>> = dao.getMedicationHistory()
    val nutritionLog: Flow<List<NutritionEntryEntity>> = dao.getAllNutritionEntries()
    val documents: Flow<List<MedicalDocumentEntity>> = dao.getAllDocuments()

    suspend fun saveProfile(profile: PatientProfileEntity) = dao.insertOrUpdateProfile(profile)
    suspend fun saveMedicalInfo(info: MedicalInfoEntity) = dao.insertOrUpdateMedicalInfo(info)
    suspend fun savePassport(passport: DialysisPassportEntity) = dao.insertOrUpdatePassport(passport)
    suspend fun addSession(session: DialysisSessionEntity) = dao.insertSession(session)
    suspend fun deleteSession(session: DialysisSessionEntity) = dao.deleteSession(session)
    suspend fun addHomeMonitoring(entry: HomeMonitoringEntity) = dao.insertHomeMonitoring(entry)
    suspend fun addLabResult(lab: LabResultEntity) = dao.insertLabResult(lab)
    suspend fun addMedication(medication: MedicationEntity) = dao.insertMedication(medication)
    suspend fun updateMedication(medication: MedicationEntity) = dao.updateMedication(medication)
    suspend fun addNutritionEntry(entry: NutritionEntryEntity) = dao.insertNutritionEntry(entry)
    suspend fun addDocument(doc: MedicalDocumentEntity) = dao.insertDocument(doc)
}
