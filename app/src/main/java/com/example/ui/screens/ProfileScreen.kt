package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.MedicalInfoEntity
import com.example.data.model.PatientProfileEntity
import com.example.ui.language.LanguageManager
import com.example.ui.theme.DialysisBluePrimary
import com.example.ui.theme.EmergencyRed
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val profileState by viewModel.profileState.collectAsState()
    val medicalState by viewModel.medicalInfoState.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    var isEditingProfile by remember { mutableStateOf(false) }

    val profile = profileState ?: PatientProfileEntity()
    val medical = medicalState ?: MedicalInfoEntity()

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen")
    ) {
        // Top Header
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(DialysisBluePrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${profile.firstName} ${profile.lastName}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "${profile.dateOfBirth} (${profile.age} ans) • ${profile.bloodGroup}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text(LanguageManager.getString("profile"), style = MaterialTheme.typography.titleMedium) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text(LanguageManager.getString("medical_info"), style = MaterialTheme.typography.titleMedium) }
                    )
                }
            }
        }

        // Tab Content
        if (selectedTab == 0) {
            PatientProfileTab(profile = profile, onSave = { updated -> viewModel.saveProfile(updated) })
        } else {
            MedicalInfoTab(medical = medical, onSave = { updated -> viewModel.saveMedicalInfo(updated) })
        }
    }
}

@Composable
private fun PatientProfileTab(
    profile: PatientProfileEntity,
    onSave: (PatientProfileEntity) -> Unit
) {
    var firstName by remember(profile) { mutableStateOf(profile.firstName) }
    var lastName by remember(profile) { mutableStateOf(profile.lastName) }
    var dob by remember(profile) { mutableStateOf(profile.dateOfBirth) }
    var age by remember(profile) { mutableStateOf(profile.age.toString()) }
    var gender by remember(profile) { mutableStateOf(profile.gender) }
    var bloodGroup by remember(profile) { mutableStateOf(profile.bloodGroup) }
    var phone by remember(profile) { mutableStateOf(profile.phone) }
    var emergencyContact by remember(profile) { mutableStateOf(profile.emergencyContact) }
    var address by remember(profile) { mutableStateOf(profile.address) }
    var nationalId by remember(profile) { mutableStateOf(profile.nationalId) }
    var insurance by remember(profile) { mutableStateOf(profile.medicalInsurance) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text(LanguageManager.getString("first_name")) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_first_name")
            )
        }
        item {
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text(LanguageManager.getString("last_name")) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_last_name")
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = dob,
                    onValueChange = { dob = it },
                    label = { Text(LanguageManager.getString("dob")) },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text(LanguageManager.getString("age")) },
                    modifier = Modifier.weight(0.6f)
                )
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = bloodGroup,
                    onValueChange = { bloodGroup = it },
                    label = { Text(LanguageManager.getString("blood_group")) },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = gender,
                    onValueChange = { gender = it },
                    label = { Text(LanguageManager.getString("gender")) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        item {
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text(LanguageManager.getString("phone")) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = emergencyContact,
                onValueChange = { emergencyContact = it },
                label = { Text(LanguageManager.getString("emergency_contact")) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text(LanguageManager.getString("address")) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = nationalId,
                onValueChange = { nationalId = it },
                label = { Text(LanguageManager.getString("national_id")) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = insurance,
                onValueChange = { insurance = it },
                label = { Text(LanguageManager.getString("insurance")) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Button(
                onClick = {
                    onSave(
                        profile.copy(
                            firstName = firstName,
                            lastName = lastName,
                            dateOfBirth = dob,
                            age = age.toIntOrNull() ?: profile.age,
                            gender = gender,
                            bloodGroup = bloodGroup,
                            phone = phone,
                            emergencyContact = emergencyContact,
                            address = address,
                            nationalId = nationalId,
                            medicalInsurance = insurance
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("save_profile_button")
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
                Spacer(modifier = Modifier.width(8.dp))
                Text(LanguageManager.getString("save"), style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
private fun MedicalInfoTab(
    medical: MedicalInfoEntity,
    onSave: (MedicalInfoEntity) -> Unit
) {
    var diagnosis by remember(medical) { mutableStateOf(medical.diagnosis) }
    var cause by remember(medical) { mutableStateOf(medical.causeOfKidneyFailure) }
    var startDate by remember(medical) { mutableStateOf(medical.dateDialysisStarted) }
    var center by remember(medical) { mutableStateOf(medical.dialysisCenter) }
    var nephrologist by remember(medical) { mutableStateOf(medical.treatingNephrologist) }
    var gp by remember(medical) { mutableStateOf(medical.generalPractitioner) }
    var allergies by remember(medical) { mutableStateOf(medical.allergies) }
    var surgeries by remember(medical) { mutableStateOf(medical.pastSurgeries) }
    var chronic by remember(medical) { mutableStateOf(medical.chronicDiseases) }
    var vaccinations by remember(medical) { mutableStateOf(medical.vaccinations) }
    var emergencyInfo by remember(medical) { mutableStateOf(medical.emergencyInformation) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            OutlinedTextField(
                value = diagnosis,
                onValueChange = { diagnosis = it },
                label = { Text(LanguageManager.getString("diagnosis")) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = cause,
                onValueChange = { cause = it },
                label = { Text(LanguageManager.getString("kidney_failure_cause")) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = startDate,
                onValueChange = { startDate = it },
                label = { Text(LanguageManager.getString("dialysis_start_date")) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = center,
                onValueChange = { center = it },
                label = { Text(LanguageManager.getString("dialysis_center")) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = nephrologist,
                onValueChange = { nephrologist = it },
                label = { Text(LanguageManager.getString("nephrologist")) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = gp,
                onValueChange = { gp = it },
                label = { Text(LanguageManager.getString("general_practitioner")) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = allergies,
                onValueChange = { allergies = it },
                label = { Text(LanguageManager.getString("allergies")) },
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmergencyRed),
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = surgeries,
                onValueChange = { surgeries = it },
                label = { Text(LanguageManager.getString("past_surgeries")) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = chronic,
                onValueChange = { chronic = it },
                label = { Text(LanguageManager.getString("chronic_diseases")) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = vaccinations,
                onValueChange = { vaccinations = it },
                label = { Text(LanguageManager.getString("vaccinations")) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = emergencyInfo,
                onValueChange = { emergencyInfo = it },
                label = { Text(LanguageManager.getString("emergency_notes")) },
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Button(
                onClick = {
                    onSave(
                        medical.copy(
                            diagnosis = diagnosis,
                            causeOfKidneyFailure = cause,
                            dateDialysisStarted = startDate,
                            dialysisCenter = center,
                            treatingNephrologist = nephrologist,
                            generalPractitioner = gp,
                            allergies = allergies,
                            pastSurgeries = surgeries,
                            chronicDiseases = chronic,
                            vaccinations = vaccinations,
                            emergencyInformation = emergencyInfo
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("save_medical_info_button")
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
                Spacer(modifier = Modifier.width(8.dp))
                Text(LanguageManager.getString("save"), style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
