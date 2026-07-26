package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.MedicationEntity
import com.example.ui.language.LanguageManager
import com.example.ui.theme.DialysisBluePrimary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun MedicationsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val activeMeds by viewModel.activeMedsState.collectAsState()
    val medHistory by viewModel.medHistoryState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add Med") },
                text = { Text("Ajouter Médicament") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.testTag("add_med_fab")
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .testTag("medications_screen")
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = LanguageManager.getString("meds"),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Traitements actuels, chélateurs de phosphore et rappels",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Traitements Actuels (${activeMeds.size})") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Historique (${medHistory.size})") }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            val currentList = if (selectedTab == 0) activeMeds else medHistory

            if (currentList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Aucun médicament répertorié.")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(currentList) { med ->
                        MedicationCard(
                            medication = med,
                            onToggleReminder = { viewModel.toggleMedicationReminder(med) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddMedicationDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { newMed ->
                viewModel.addMedication(newMed)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun MedicationCard(
    medication: MedicationEntity,
    onToggleReminder: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DialysisBluePrimary.copy(alpha = 0.1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Medication,
                        contentDescription = "Med",
                        tint = DialysisBluePrimary,
                        modifier = Modifier.padding(10.dp).size(28.dp)
                    )
                }
                Column {
                    Text(
                        text = medication.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Dose: ${medication.dose} • ${medication.schedule}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Heures: ${medication.timeString}",
                        style = MaterialTheme.typography.bodySmall,
                        color = DialysisBluePrimary
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onToggleReminder) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Reminder",
                        tint = if (medication.reminderEnabled) DialysisBluePrimary else Color.Gray
                    )
                }
                Text(
                    text = if (medication.reminderEnabled) "Rappel Actif" else "Inactif",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (medication.reminderEnabled) DialysisBluePrimary else Color.Gray
                )
            }
        }
    }
}

@Composable
private fun AddMedicationDialog(
    onDismiss: () -> Unit,
    onConfirm: (MedicationEntity) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var dose by remember { mutableStateOf("") }
    var schedule by remember { mutableStateOf("Au milieu des repas (3x/jour)") }
    var timeString by remember { mutableStateOf("08:00, 13:00, 20:00") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ajouter Médicament") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom du médicament (ex: Renagel)") },
                    modifier = Modifier.fillMaxWidth().testTag("input_med_name")
                )
                OutlinedTextField(
                    value = dose,
                    onValueChange = { dose = it },
                    label = { Text("Dosage (ex: 800 mg)") },
                    modifier = Modifier.fillMaxWidth().testTag("input_med_dose")
                )
                OutlinedTextField(
                    value = schedule,
                    onValueChange = { schedule = it },
                    label = { Text("Posologie / Fréquence") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = timeString,
                    onValueChange = { timeString = it },
                    label = { Text("Heures de prise") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(
                            MedicationEntity(
                                name = name,
                                dose = dose,
                                schedule = schedule,
                                timeString = timeString
                            )
                        )
                    }
                },
                modifier = Modifier.testTag("confirm_add_med")
            ) {
                Text(LanguageManager.getString("save"))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(LanguageManager.getString("cancel"))
            }
        }
    )
}
