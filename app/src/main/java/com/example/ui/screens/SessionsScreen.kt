package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.DialysisSessionEntity
import com.example.ui.language.LanguageManager
import com.example.ui.theme.DialysisBluePrimary
import com.example.ui.theme.DialysisTealLight
import com.example.ui.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SessionsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val sessions by viewModel.sessionsState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                text = { Text(LanguageManager.getString("new_session")) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.testTag("add_session_fab")
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .testTag("sessions_screen")
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Historique des Séances de Dialyse",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Une fiche complète paramétrée pour chaque séance",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (sessions.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Aucune séance enregistrée pour l'instant.")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(sessions) { session ->
                        SessionItemCard(session = session, onDelete = { viewModel.deleteSession(session) })
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddSessionDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { newSession ->
                viewModel.addSession(newSession)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun SessionItemCard(
    session: DialysisSessionEntity,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = "Date",
                        tint = DialysisBluePrimary
                    )
                    Text(
                        text = "Séance du ${session.dateString}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
                }
            }

            Text(
                text = "Hôpital: ${session.hospital} • Machine N° ${session.machineNumber}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
            Text(
                text = "Infirmière: ${session.nurseName} • Médecin: ${session.doctorName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(12.dp))
            Divider()
            Spacer(modifier = Modifier.height(12.dp))

            // 3 Columns: Before - After - UF
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Avant Dialyse", style = MaterialTheme.typography.labelMedium, color = DialysisBluePrimary)
                    Text(text = "Poids: ${session.weightBeforeKg} kg", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "TA: ${session.bpBeforeSys}/${session.bpBeforeDia}", style = MaterialTheme.typography.bodyMedium)
                }

                Column {
                    Text(text = "Après Dialyse", style = MaterialTheme.typography.labelMedium, color = Color(0xFF00796B))
                    Text(text = "Poids: ${session.weightAfterKg} kg", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "TA: ${session.bpAfterSys}/${session.bpAfterDia}", style = MaterialTheme.typography.bodyMedium)
                }

                Surface(
                    color = DialysisTealLight,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Ultrafiltration", style = MaterialTheme.typography.labelSmall, color = Color(0xFF004D40))
                        Text(text = "${session.ultrafiltrationLiters} L", style = MaterialTheme.typography.titleMedium, color = Color(0xFF004D40))
                    }
                }
            }

            if (session.symptomsDuring.isNotBlank() || session.commentsBefore.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Notes: ${session.symptomsDuring.ifBlank { session.commentsBefore }}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddSessionDialog(
    onDismiss: () -> Unit,
    onConfirm: (DialysisSessionEntity) -> Unit
) {
    var hospital by remember { mutableStateOf("Centre de Dialyse El Shifa") }
    var nurse by remember { mutableStateOf("Infirmière Fatma") }
    var doctor by remember { mutableStateOf("Dr. Mansouri") }
    var machine by remember { mutableStateOf("Fresenius 5008 - M02") }

    var weightBefore by remember { mutableStateOf("70.5") }
    var bpBeforeSys by remember { mutableStateOf("145") }
    var bpBeforeDia by remember { mutableStateOf("88") }
    var hrBefore by remember { mutableStateOf("78") }
    var tempBefore by remember { mutableStateOf("36.6") }

    var symptomsDuring by remember { mutableStateOf("") }
    var medsAdministered by remember { mutableStateOf("EPO 4000 UI IV") }

    var weightAfter by remember { mutableStateOf("68.4") }
    var bpAfterSys by remember { mutableStateOf("125") }
    var bpAfterDia by remember { mutableStateOf("78") }
    var generalCondition by remember { mutableStateOf("Bonne tolérance") }
    var recommendations by remember { mutableStateOf("Restriction eau 800ml/jour") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(LanguageManager.getString("new_session")) },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Text("Infirmière & Machine", style = MaterialTheme.typography.titleSmall, color = DialysisBluePrimary)
                }
                item {
                    OutlinedTextField(value = hospital, onValueChange = { hospital = it }, label = { Text("Hôpital / Centre") }, modifier = Modifier.fillMaxWidth())
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = nurse, onValueChange = { nurse = it }, label = { Text("Infirmière") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = machine, onValueChange = { machine = it }, label = { Text("Machine N°") }, modifier = Modifier.weight(1f))
                    }
                }

                item { Divider(); Text("1. Avant Dialyse", style = MaterialTheme.typography.titleSmall, color = DialysisBluePrimary) }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = weightBefore, onValueChange = { weightBefore = it }, label = { Text("Poids Avant (kg)") }, modifier = Modifier.weight(1f).testTag("input_weight_before"))
                        OutlinedTextField(value = bpBeforeSys, onValueChange = { bpBeforeSys = it }, label = { Text("TA Sys") }, modifier = Modifier.weight(0.5f))
                        OutlinedTextField(value = bpBeforeDia, onValueChange = { bpBeforeDia = it }, label = { Text("TA Dia") }, modifier = Modifier.weight(0.5f))
                    }
                }

                item { Divider(); Text("2. Pendant Dialyse", style = MaterialTheme.typography.titleSmall, color = DialysisBluePrimary) }
                item {
                    OutlinedTextField(value = symptomsDuring, onValueChange = { symptomsDuring = it }, label = { Text("Symptômes / Incidents") }, modifier = Modifier.fillMaxWidth())
                }
                item {
                    OutlinedTextField(value = medsAdministered, onValueChange = { medsAdministered = it }, label = { Text("Médicaments injectés") }, modifier = Modifier.fillMaxWidth())
                }

                item { Divider(); Text("3. Après Dialyse", style = MaterialTheme.typography.titleSmall, color = DialysisBluePrimary) }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = weightAfter, onValueChange = { weightAfter = it }, label = { Text("Poids Après (kg)") }, modifier = Modifier.weight(1f).testTag("input_weight_after"))
                        OutlinedTextField(value = bpAfterSys, onValueChange = { bpAfterSys = it }, label = { Text("TA Sys") }, modifier = Modifier.weight(0.5f))
                        OutlinedTextField(value = bpAfterDia, onValueChange = { bpAfterDia = it }, label = { Text("TA Dia") }, modifier = Modifier.weight(0.5f))
                    }
                }
                item {
                    val wB = weightBefore.toDoubleOrNull() ?: 0.0
                    val wA = weightAfter.toDoubleOrNull() ?: 0.0
                    val calculatedUf = (wB - wA).coerceAtLeast(0.0)
                    Text(text = "Ultrafiltration Calculée: ${String.format(Locale.ENGLISH, "%.2f", calculatedUf)} Litres", style = MaterialTheme.typography.titleMedium, color = Color(0xFF00796B))
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val wB = weightBefore.toDoubleOrNull() ?: 70.0
                    val wA = weightAfter.toDoubleOrNull() ?: 68.0
                    val calculatedUf = (wB - wA).coerceAtLeast(0.0)

                    onConfirm(
                        DialysisSessionEntity(
                            hospital = hospital,
                            nurseName = nurse,
                            doctorName = doctor,
                            machineNumber = machine,
                            weightBeforeKg = wB,
                            bpBeforeSys = bpBeforeSys.toIntOrNull() ?: 140,
                            bpBeforeDia = bpBeforeDia.toIntOrNull() ?: 85,
                            hrBefore = hrBefore.toIntOrNull() ?: 75,
                            tempBefore = tempBefore.toDoubleOrNull() ?: 36.6,
                            symptomsDuring = symptomsDuring,
                            medsAdministered = medsAdministered,
                            weightAfterKg = wA,
                            bpAfterSys = bpAfterSys.toIntOrNull() ?: 125,
                            bpAfterDia = bpAfterDia.toIntOrNull() ?: 78,
                            ultrafiltrationLiters = calculatedUf,
                            generalCondition = generalCondition,
                            recommendations = recommendations
                        )
                    )
                },
                modifier = Modifier.testTag("confirm_add_session")
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
