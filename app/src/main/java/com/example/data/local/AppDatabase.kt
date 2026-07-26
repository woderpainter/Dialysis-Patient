package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        PatientProfileEntity::class,
        MedicalInfoEntity::class,
        DialysisPassportEntity::class,
        DialysisSessionEntity::class,
        HomeMonitoringEntity::class,
        LabResultEntity::class,
        MedicationEntity::class,
        NutritionEntryEntity::class,
        MedicalDocumentEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dialysisDao(): DialysisDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dialysis_book_db"
                )
                    .addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database.dialysisDao())
                    }
                }
            }
        }

        suspend fun populateInitialData(dao: DialysisDao) {
            // Seed Profile
            dao.insertOrUpdateProfile(PatientProfileEntity())

            // Seed Medical Info
            dao.insertOrUpdateMedicalInfo(MedicalInfoEntity())

            // Seed Passport
            dao.insertOrUpdatePassport(DialysisPassportEntity())

            // Seed Initial Dialysis Sessions
            val now = System.currentTimeMillis()
            val dayMs = 86400000L
            dao.insertSession(
                DialysisSessionEntity(
                    dateTimestamp = now - 2 * dayMs,
                    dateString = "2026-07-24",
                    weightBeforeKg = 70.8,
                    bpBeforeSys = 145,
                    bpBeforeDia = 86,
                    weightAfterKg = 68.5,
                    bpAfterSys = 125,
                    bpAfterDia = 78,
                    ultrafiltrationLiters = 2.3,
                    commentsBefore = "Good session start. Dry weight 68.5kg.",
                    generalCondition = "Post-dialysis condition excellent. No hypotension."
                )
            )
            dao.insertSession(
                DialysisSessionEntity(
                    dateTimestamp = now - 4 * dayMs,
                    dateString = "2026-07-22",
                    weightBeforeKg = 71.0,
                    bpBeforeSys = 150,
                    bpBeforeDia = 90,
                    weightAfterKg = 68.6,
                    bpAfterSys = 128,
                    bpAfterDia = 80,
                    ultrafiltrationLiters = 2.4,
                    commentsBefore = "Regular Monday/Wed session.",
                    generalCondition = "Stable"
                )
            )

            // Seed Home Monitoring
            dao.insertHomeMonitoring(
                HomeMonitoringEntity(
                    timestamp = now - dayMs,
                    dateString = "2026-07-25",
                    bpMorningSys = 132, bpMorningDia = 80,
                    bpNoonSys = 136, bpNoonDia = 82,
                    bpEveningSys = 130, bpEveningDia = 78,
                    bpNightSys = 126, bpNightDia = 76,
                    weightKg = 69.4,
                    bloodSugarG_L = 1.28
                )
            )
            dao.insertHomeMonitoring(
                HomeMonitoringEntity(
                    timestamp = now - 2 * dayMs,
                    dateString = "2026-07-24",
                    bpMorningSys = 138, bpMorningDia = 85,
                    weightKg = 70.8,
                    bloodSugarG_L = 1.42
                )
            )

            // Seed Lab Results
            dao.insertLabResult(
                LabResultEntity(
                    dateTimestamp = now - 10 * dayMs,
                    dateString = "2026-07-16",
                    hemoglobinG_dL = 11.2,
                    creatinineMg_dL = 8.2,
                    ureaG_L = 1.35,
                    potassiumMeq_L = 5.1,
                    calciumMg_dL = 9.2,
                    phosphorusMg_dL = 5.3,
                    albuminG_L = 39.0,
                    ferritinNg_mL = 410.0,
                    crpMg_L = 3.2,
                    notes = "Monthly routine check - parameters within dialysis target range."
                )
            )
            dao.insertLabResult(
                LabResultEntity(
                    dateTimestamp = now - 40 * dayMs,
                    dateString = "2026-06-15",
                    hemoglobinG_dL = 10.4,
                    creatinineMg_dL = 8.8,
                    ureaG_L = 1.50,
                    potassiumMeq_L = 5.7, // High Potassium Alert
                    calciumMg_dL = 8.8,
                    phosphorusMg_dL = 5.8, // High Phosphorus Alert
                    albuminG_L = 37.0,
                    ferritinNg_mL = 350.0,
                    crpMg_L = 6.0,
                    notes = "Hyperkalemia warning (5.7 meq/L). Dietary potassium restriction advised."
                )
            )

            // Seed Medications
            dao.insertMedication(MedicationEntity(name = "Renagel (Sevelamer)", dose = "800 mg", schedule = "1 tablet 3x daily with meals", timeString = "08:00, 13:00, 20:00"))
            dao.insertMedication(MedicationEntity(name = "Amlodipine", dose = "5 mg", schedule = "1 tablet morning", timeString = "08:00"))
            dao.insertMedication(MedicationEntity(name = "Tahor (Atorvastatin)", dose = "20 mg", schedule = "1 tablet night", timeString = "21:00"))
            dao.insertMedication(MedicationEntity(name = "Alpha D3 (Alfacalcidol)", dose = "0.25 mcg", schedule = "1 capsule daily", timeString = "08:00"))
            dao.insertMedication(MedicationEntity(name = "Recormon (EPO)", dose = "4000 UI", schedule = "Inject 2x/week during dialysis session", timeString = "Mon, Fri"))

            // Seed Nutrition
            dao.insertNutritionEntry(
                NutritionEntryEntity(
                    timestamp = now - dayMs,
                    dateString = "2026-07-25",
                    fluidIntakeMl = 700,
                    foodLog = "Tea, Rice with grilled turkey, Steamed zucchini, Pear (half)",
                    notes = "Good fluid management"
                )
            )

            // Seed Medical Documents
            dao.insertDocument(
                MedicalDocumentEntity(
                    title = "Echocardiogram Report",
                    category = "Radiology",
                    dateString = "2026-05-20",
                    summary = "EF 60%, mild LVH, no pericardial effusion."
                )
            )
            dao.insertDocument(
                MedicalDocumentEntity(
                    title = "AV Fistula Doppler Ultrasound",
                    category = "Radiology",
                    dateString = "2026-03-12",
                    summary = "Left radiocephalic fistula patent, flow 850 mL/min."
                )
            )
        }
    }
}
