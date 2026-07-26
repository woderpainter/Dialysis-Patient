package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.DialysisPassportEntity
import com.example.ui.language.LanguageManager
import com.example.ui.theme.DialysisBluePrimary
import com.example.ui.theme.DialysisTealLight
import com.example.ui.viewmodel.MainViewModel

@Composable
fun PassportScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val passportState by viewModel.passportState.collectAsState()
    val passport = passportState ?: DialysisPassportEntity()

    var dryWeight by remember(passport) { mutableStateOf(passport.dryWeightKg.toString()) }
    var duration by remember(passport) { mutableStateOf(passport.durationHours.toString()) }
    var mon by remember(passport) { mutableStateOf(passport.monSchedule) }
    var tue by remember(passport) { mutableStateOf(passport.tueSchedule) }
    var wed by remember(passport) { mutableStateOf(passport.wedSchedule) }
    var thu by remember(passport) { mutableStateOf(passport.thuSchedule) }
    var fri by remember(passport) { mutableStateOf(passport.friSchedule) }
    var sat by remember(passport) { mutableStateOf(passport.satSchedule) }
    var sun by remember(passport) { mutableStateOf(passport.sunSchedule) }
    var scheduleTime by remember(passport) { mutableStateOf(passport.scheduleTime) }
    var accessType by remember(passport) { mutableStateOf(passport.vascularAccessType) }
    var accessLocation by remember(passport) { mutableStateOf(passport.accessLocation) }
    var bloodFlow by remember(passport) { mutableStateOf(passport.bloodFlowMlMin.toString()) }
    var dialysateFlow by remember(passport) { mutableStateOf(passport.dialysateFlowMlMin.toString()) }
    var heparinProtocol by remember(passport) { mutableStateOf(passport.heparinProtocol) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("passport_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title Header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = "Passport",
                        tint = DialysisBluePrimary,
                        modifier = Modifier.size(36.dp)
                    )
                    Column {
                        Text(
                            text = LanguageManager.getString("passport"),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Prescription d'Hémodialyse & Accès Vasculaire",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Dry Weight Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = LanguageManager.getString("dry_weight"),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = dryWeight,
                            onValueChange = { dryWeight = it },
                            label = { Text("Poids Sec (kg)") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_dry_weight")
                        )
                        OutlinedTextField(
                            value = duration,
                            onValueChange = { duration = it },
                            label = { Text("Durée (Heures)") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Dialysis Schedule Days
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = LanguageManager.getString("dialysis_schedule"),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Jours de séances hebdomadaires:", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FilterChip(selected = mon, onClick = { mon = !mon }, label = { Text("Lun") })
                        FilterChip(selected = tue, onClick = { tue = !tue }, label = { Text("Mar") })
                        FilterChip(selected = wed, onClick = { wed = !wed }, label = { Text("Mer") })
                        FilterChip(selected = thu, onClick = { thu = !thu }, label = { Text("Jeu") })
                        FilterChip(selected = fri, onClick = { fri = !fri }, label = { Text("Ven") })
                        FilterChip(selected = sat, onClick = { sat = !sat }, label = { Text("Sam") })
                        FilterChip(selected = sun, onClick = { sun = !sun }, label = { Text("Dim") })
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = scheduleTime,
                        onValueChange = { scheduleTime = it },
                        label = { Text("Plage Horaire") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Vascular Access
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = LanguageManager.getString("vascular_access"),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val types = listOf("AV Fistula", "Catheter", "Graft")
                        types.forEach { type ->
                            FilterChip(
                                selected = accessType == type,
                                onClick = { accessType = type },
                                label = { Text(type) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = accessLocation,
                        onValueChange = { accessLocation = it },
                        label = { Text(LanguageManager.getString("access_location")) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Dialysis Prescription
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = LanguageManager.getString("prescription"),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = bloodFlow,
                            onValueChange = { bloodFlow = it },
                            label = { Text("Débit Sang (Qb mL/min)") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = dialysateFlow,
                            onValueChange = { dialysateFlow = it },
                            label = { Text("Débit Dialysat (Qd mL/min)") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = heparinProtocol,
                        onValueChange = { heparinProtocol = it },
                        label = { Text(LanguageManager.getString("heparin_protocol")) },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Save Button
        item {
            Button(
                onClick = {
                    viewModel.savePassport(
                        passport.copy(
                            dryWeightKg = dryWeight.toDoubleOrNull() ?: passport.dryWeightKg,
                            durationHours = duration.toDoubleOrNull() ?: passport.durationHours,
                            monSchedule = mon,
                            tueSchedule = tue,
                            wedSchedule = wed,
                            thuSchedule = thu,
                            friSchedule = fri,
                            satSchedule = sat,
                            sunSchedule = sun,
                            scheduleTime = scheduleTime,
                            vascularAccessType = accessType,
                            accessLocation = accessLocation,
                            bloodFlowMlMin = bloodFlow.toIntOrNull() ?: passport.bloodFlowMlMin,
                            dialysateFlowMlMin = dialysateFlow.toIntOrNull() ?: passport.dialysateFlowMlMin,
                            heparinProtocol = heparinProtocol
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("save_passport_button")
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
                Spacer(modifier = Modifier.width(8.dp))
                Text(LanguageManager.getString("save"), style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
