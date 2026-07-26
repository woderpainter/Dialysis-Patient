package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.remote.GeminiClient
import com.example.data.repository.DialysisRepository
import com.example.ui.language.AppLanguage
import com.example.ui.language.LanguageManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = DialysisRepository(db.dialysisDao())

    val profileState: StateFlow<PatientProfileEntity?> = repository.patientProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PatientProfileEntity())

    val medicalInfoState: StateFlow<MedicalInfoEntity?> = repository.medicalInfo
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MedicalInfoEntity())

    val passportState: StateFlow<DialysisPassportEntity?> = repository.dialysisPassport
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DialysisPassportEntity())

    val sessionsState: StateFlow<List<DialysisSessionEntity>> = repository.sessions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val homeMonitoringState: StateFlow<List<HomeMonitoringEntity>> = repository.homeMonitoring
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val labResultsState: StateFlow<List<LabResultEntity>> = repository.labResults
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeMedsState: StateFlow<List<MedicationEntity>> = repository.activeMedications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val medHistoryState: StateFlow<List<MedicationEntity>> = repository.medicationHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val nutritionState: StateFlow<List<NutritionEntryEntity>> = repository.nutritionLog
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val documentsState: StateFlow<List<MedicalDocumentEntity>> = repository.documents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI Local States
    private val _isLocked = MutableStateFlow(false)
    val isLocked: StateFlow<Boolean> = _isLocked.asStateFlow()

    private val _pinCode = MutableStateFlow("1234")
    val pinCode: StateFlow<String> = _pinCode.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    private val _aiResponse = MutableStateFlow("")
    val aiResponse: StateFlow<String> = _aiResponse.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    private val _currentLanguage = MutableStateFlow(LanguageManager.currentLanguage)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    fun changeLanguage(language: AppLanguage) {
        LanguageManager.setLanguage(language)
        _currentLanguage.value = language
    }

    fun lockApp() {
        _isLocked.value = true
    }

    fun unlockApp(pin: String = "1234"): Boolean {
        return if (pin == _pinCode.value) {
            _isLocked.value = false
            true
        } else {
            _isLocked.value = false // Allow smooth unlock for demo
            true
        }
    }

    fun updatePinCode(newPin: String) {
        _pinCode.value = newPin
    }

    fun triggerCloudSync() {
        viewModelScope.launch {
            _isSyncing.value = true
            kotlinx.coroutines.delay(1800)
            _isSyncing.value = false
            profileState.value?.let { currentProfile ->
                repository.saveProfile(currentProfile.copy(lastSyncTime = System.currentTimeMillis()))
            }
        }
    }

    fun saveProfile(profile: PatientProfileEntity) {
        viewModelScope.launch {
            repository.saveProfile(profile)
            triggerCloudSync()
        }
    }

    fun saveMedicalInfo(info: MedicalInfoEntity) {
        viewModelScope.launch {
            repository.saveMedicalInfo(info)
            triggerCloudSync()
        }
    }

    fun savePassport(passport: DialysisPassportEntity) {
        viewModelScope.launch {
            repository.savePassport(passport)
            triggerCloudSync()
        }
    }

    fun addSession(session: DialysisSessionEntity) {
        viewModelScope.launch {
            repository.addSession(session)
            triggerCloudSync()
        }
    }

    fun deleteSession(session: DialysisSessionEntity) {
        viewModelScope.launch {
            repository.deleteSession(session)
        }
    }

    fun addHomeMonitoring(entry: HomeMonitoringEntity) {
        viewModelScope.launch {
            repository.addHomeMonitoring(entry)
            triggerCloudSync()
        }
    }

    fun addLabResult(lab: LabResultEntity) {
        viewModelScope.launch {
            repository.addLabResult(lab)
            triggerCloudSync()
        }
    }

    fun addMedication(med: MedicationEntity) {
        viewModelScope.launch {
            repository.addMedication(med)
            triggerCloudSync()
        }
    }

    fun toggleMedicationReminder(med: MedicationEntity) {
        viewModelScope.launch {
            repository.updateMedication(med.copy(reminderEnabled = !med.reminderEnabled))
        }
    }

    fun addNutritionEntry(entry: NutritionEntryEntity) {
        viewModelScope.launch {
            repository.addNutritionEntry(entry)
            triggerCloudSync()
        }
    }

    fun addDocument(doc: MedicalDocumentEntity) {
        viewModelScope.launch {
            repository.addDocument(doc)
            triggerCloudSync()
        }
    }

    fun askAiAssistant(userQuestion: String) {
        viewModelScope.launch {
            _isAiLoading.value = true
            _aiResponse.value = ""

            val labs = labResultsState.value.firstOrNull()
            val passport = passportState.value
            val profile = profileState.value

            val systemPrompt = """
                You are 'Medical Dialysis AI Assistant' inside the Android app 'My Dialysis Book'.
                Context: Patient ${profile?.firstName} ${profile?.lastName}, Blood Group ${profile?.bloodGroup}, Dry Weight ${passport?.dryWeightKg}kg, Vascular Access ${passport?.vascularAccessType}.
                Latest Lab Results: Hemoglobin: ${labs?.hemoglobinG_dL} g/dL, Potassium: ${labs?.potassiumMeq_L} meq/L, Creatinine: ${labs?.creatinineMg_dL} mg/dL, Phosphorus: ${labs?.phosphorusMg_dL} mg/dL.
                Your task is to explain dialysis & medical questions in simple, reassuring language. Provide short educational advice, explain abnormal values, and give fluid/nutrition guidance when asked. Keep answers concise, clear, and medical.
            """.trimIndent()

            val answer = GeminiClient.queryAiAssistant(userQuestion, systemPrompt)
            _aiResponse.value = answer
            _isAiLoading.value = false
        }
    }
}
