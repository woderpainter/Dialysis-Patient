package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "patient_profile")
data class PatientProfileEntity(
    @PrimaryKey val id: Int = 1,
    val firstName: String = "Mohammed",
    val lastName: String = "Benali",
    val dateOfBirth: String = "15/04/1962",
    val age: Int = 62,
    val gender: String = "Male", // Male / Female / Homme / Femme
    val bloodGroup: String = "O+",
    val phone: String = "+213 661 234 567",
    val emergencyContact: String = "Amina Benali (Wife) - +213 661 987 654",
    val address: String = "12 Rue de la Liberté, Alger",
    val nationalId: String = "10982374921",
    val medicalInsurance: String = "CNAS N° 8234901239",
    val photoUri: String = "",
    val qrCodeId: String = "PAT-2026-ALG-8892",
    val cloudSynced: Boolean = true,
    val lastSyncTime: Long = System.currentTimeMillis()
)

@Entity(tableName = "medical_info")
data class MedicalInfoEntity(
    @PrimaryKey val id: Int = 1,
    val diagnosis: String = "End-Stage Renal Disease (ESRD) - Stage 5 CKD",
    val causeOfKidneyFailure: String = "Diabetic Nephropathy & Long-standing Hypertension",
    val dateDialysisStarted: String = "10/01/2023",
    val dialysisCenter: String = "Centre de Dialyse El Shifa, Alger",
    val treatingNephrologist: String = "Dr. Karim Mansouri",
    val generalPractitioner: String = "Dr. Sarah Larbi",
    val bloodGroup: String = "O+",
    val allergies: String = "Penicillin, Iodine Contrast Media",
    val pastSurgeries: String = "AV Fistula Creation (Left Forearm - Nov 2022)",
    val chronicDiseases: String = "Type 2 Diabetes Mellitus, Arterial Hypertension",
    val vaccinations: String = "Hepatitis B (4 doses - 2022), Influenza (Oct 2025), Pneumococcal",
    val emergencyInformation: String = "Fistula in Left Arm - NO IV/BP on Left Arm! High risk of hyperkalemia."
)

@Entity(tableName = "dialysis_passport")
data class DialysisPassportEntity(
    @PrimaryKey val id: Int = 1,
    val dryWeightKg: Double = 68.5,
    val monSchedule: Boolean = true,
    val tueSchedule: Boolean = false,
    val wedSchedule: Boolean = true,
    val thuSchedule: Boolean = false,
    val friSchedule: Boolean = true,
    val satSchedule: Boolean = false,
    val sunSchedule: Boolean = false,
    val scheduleTime: String = "08:00 AM - 12:00 PM",
    val durationHours: Double = 4.0,
    val vascularAccessType: String = "AV Fistula", // Catheter, AV Fistula, Graft
    val accessLocation: String = "Left Radiocephalic Forearm",
    val bloodFlowMlMin: Int = 320, // QB
    val dialysateFlowMlMin: Int = 500, // QD
    val heparinProtocol: String = "Bolus 2000 UI at start, then 800 UI/hour. Stop 1h before end."
)

@Entity(tableName = "dialysis_sessions")
data class DialysisSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateTimestamp: Long = System.currentTimeMillis(),
    val dateString: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
    val hospital: String = "Centre de Dialyse El Shifa",
    val nurseName: String = "Infirmière Fatma",
    val doctorName: String = "Dr. Mansouri",
    val machineNumber: String = "Fresenius 5008 - M04",
    // Before Dialysis
    val weightBeforeKg: Double = 70.8,
    val bpBeforeSys: Int = 148,
    val bpBeforeDia: Int = 88,
    val hrBefore: Int = 76,
    val tempBefore: Double = 36.6,
    val bloodSugarBefore: Double = 1.45,
    val commentsBefore: String = "Patient feeling well. Interdialytic weight gain +2.3kg.",
    // During Dialysis
    val symptomsDuring: String = "Mild leg cramping at hour 3",
    val medsAdministered: String = "EPO 4000 UI IV, Iron Sucrose 100mg IV",
    val incidents: String = "None. UF rate adjusted slightly.",
    // After Dialysis
    val weightAfterKg: Double = 68.6,
    val bpAfterSys: Int = 126,
    val bpAfterDia: Int = 78,
    val ultrafiltrationLiters: Double = 2.2,
    val generalCondition: String = "Good, stable parameters post-dialysis",
    val recommendations: String = "Restrict fluid to 750ml/day. Continue Renagel with meals.",
    val nextSessionDate: String = "2026-07-28"
)

@Entity(tableName = "home_monitoring")
data class HomeMonitoringEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val dateString: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
    val bpMorningSys: Int = 135,
    val bpMorningDia: Int = 82,
    val bpNoonSys: Int = 138,
    val bpNoonDia: Int = 84,
    val bpEveningSys: Int = 132,
    val bpEveningDia: Int = 80,
    val bpNightSys: Int = 128,
    val bpNightDia: Int = 78,
    val weightKg: Double = 69.8,
    val temperatureC: Double = 36.7,
    val bloodSugarG_L: Double = 1.30,
    val symptoms: String = "No edema, good appetite"
)

@Entity(tableName = "lab_results")
data class LabResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateTimestamp: Long = System.currentTimeMillis(),
    val dateString: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
    val hemoglobinG_dL: Double = 11.2, // Normal for dialysis: 10-12
    val creatinineMg_dL: Double = 8.5,
    val ureaG_L: Double = 1.4,
    val potassiumMeq_L: Double = 5.2, // Normal: 3.5-5.5 (Alert > 5.5)
    val calciumMg_dL: Double = 9.1,
    val phosphorusMg_dL: Double = 5.4, // Alert > 5.5
    val albuminG_L: Double = 38.0,
    val ferritinNg_mL: Double = 420.0,
    val ironUg_dL: Double = 75.0,
    val crpMg_L: Double = 4.5,
    val pthPg_mL: Double = 240.0,
    val vitaminDNg_mL: Double = 32.0,
    val sodiumMeq_L: Double = 138.0,
    val whiteBloodCells: Double = 6.8,
    val platelets: Double = 210.0,
    val notes: String = "Routine monthly panel"
)

@Entity(tableName = "medications")
data class MedicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val dose: String,
    val schedule: String, // e.g. "With meals (3x/day)"
    val timeString: String = "08:00, 13:00, 20:00",
    val reminderEnabled: Boolean = true,
    val isHistory: Boolean = false,
    val notes: String = ""
)

@Entity(tableName = "nutrition_log")
data class NutritionEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val dateString: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
    val fluidIntakeMl: Int = 650,
    val foodLog: String = "Breakfast: Toast, Tea (100ml). Lunch: Grilled Chicken, Rice, Cooked Apple. Dinner: Green beans, Egg white.",
    val notes: String = "Well within fluid restriction target (800ml)"
)

@Entity(tableName = "medical_documents")
data class MedicalDocumentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // Medical Report, Lab PDF, Prescription, Radiology, Hospitalization, Photo
    val dateString: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
    val summary: String = "",
    val fileUri: String = ""
)
