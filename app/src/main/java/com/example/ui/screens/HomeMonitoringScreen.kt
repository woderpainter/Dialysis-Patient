package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.HomeMonitoringEntity
import com.example.ui.components.ChartCanvas
import com.example.ui.components.ChartPoint
import com.example.ui.language.LanguageManager
import com.example.ui.theme.DialysisBluePrimary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun HomeMonitoringScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val homeLogs by viewModel.homeMonitoringState.collectAsState()
    val passport by viewModel.passportState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    val dryWeightTarget = (passport?.dryWeightKg ?: 68.5).toFloat()

    // Build Chart Points for Weight & Blood Pressure
    val weightChartPoints = homeLogs.take(7).reversed().map {
        ChartPoint(label = it.dateString.takeLast(5), value = it.weightKg.toFloat())
    }

    val bpChartPoints = homeLogs.take(7).reversed().map {
        ChartPoint(
            label = it.dateString.takeLast(5),
            value = it.bpMorningSys.toFloat(),
            secondaryValue = it.bpMorningDia.toFloat()
        )
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add Vitals") },
                text = { Text("Ajouter Mesure Domicile") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.testTag("add_vitals_fab")
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .testTag("home_monitoring_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
        ) {
            item {
                Text(
                    text = LanguageManager.getString("home_vitals"),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Suivi de la tension 4x/jour et du poids entre les séances",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }

            // Weight Trend Chart
            item {
                ChartCanvas(
                    title = "Tendance du Poids (kg) vs Poids Sec",
                    points = weightChartPoints,
                    targetValue = dryWeightTarget,
                    targetLabel = "Poids Sec Cible",
                    unit = "kg",
                    lineColor = DialysisBluePrimary
                )
            }

            // Blood Pressure Trend Chart
            item {
                ChartCanvas(
                    title = "Tendance Tension Systolique/Diastolique (Matin)",
                    points = bpChartPoints,
                    targetValue = 130f,
                    targetLabel = "Cible Systolique",
                    unit = "mmHg",
                    lineColor = Color(0xFFE65100),
                    secondaryLineColor = Color(0xFF1976D2)
                )
            }

            item {
                Text(
                    text = "Historique des Mesures",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            items(homeLogs) { log ->
                VitalsItemCard(log = log)
            }
        }
    }

    if (showAddDialog) {
        AddVitalsDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { newEntry ->
                viewModel.addHomeMonitoring(newEntry)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun VitalsItemCard(log: HomeMonitoringEntity) {
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
                Text(text = "Date: ${log.dateString}", style = MaterialTheme.typography.titleMedium, color = DialysisBluePrimary)
                Text(text = "Poids: ${log.weightKg} kg", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Tension Artérielle 4x/Jour:", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Matin", style = MaterialTheme.typography.labelSmall)
                    Text(text = "${log.bpMorningSys}/${log.bpMorningDia}", style = MaterialTheme.typography.bodyMedium)
                }
                Column {
                    Text(text = "Midi", style = MaterialTheme.typography.labelSmall)
                    Text(text = "${log.bpNoonSys}/${log.bpNoonDia}", style = MaterialTheme.typography.bodyMedium)
                }
                Column {
                    Text(text = "Soir", style = MaterialTheme.typography.labelSmall)
                    Text(text = "${log.bpEveningSys}/${log.bpEveningDia}", style = MaterialTheme.typography.bodyMedium)
                }
                Column {
                    Text(text = "Nuit", style = MaterialTheme.typography.labelSmall)
                    Text(text = "${log.bpNightSys}/${log.bpNightDia}", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Glycémie: ${log.bloodSugarG_L} g/L", style = MaterialTheme.typography.bodySmall)
                Text(text = "Température: ${log.temperatureC} °C", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun AddVitalsDialog(
    onDismiss: () -> Unit,
    onConfirm: (HomeMonitoringEntity) -> Unit
) {
    var weight by remember { mutableStateOf("69.5") }
    var bpMornSys by remember { mutableStateOf("135") }
    var bpMornDia by remember { mutableStateOf("82") }
    var bpEveSys by remember { mutableStateOf("130") }
    var bpEveDia by remember { mutableStateOf("80") }
    var temp by remember { mutableStateOf("36.6") }
    var sugar by remember { mutableStateOf("1.30") }
    var symptoms by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ajouter Mesure Quotidienne") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Poids du jour (kg)") }, modifier = Modifier.fillMaxWidth().testTag("input_vitals_weight"))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = bpMornSys, onValueChange = { bpMornSys = it }, label = { Text("TA Matin Sys") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = bpMornDia, onValueChange = { bpMornDia = it }, label = { Text("TA Matin Dia") }, modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = bpEveSys, onValueChange = { bpEveSys = it }, label = { Text("TA Soir Sys") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = bpEveDia, onValueChange = { bpEveDia = it }, label = { Text("TA Soir Dia") }, modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = temp, onValueChange = { temp = it }, label = { Text("Temp (°C)") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = sugar, onValueChange = { sugar = it }, label = { Text("Glycémie (g/L)") }, modifier = Modifier.weight(1f))
                }
                OutlinedTextField(value = symptoms, onValueChange = { symptoms = it }, label = { Text("Symptômes") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        HomeMonitoringEntity(
                            weightKg = weight.toDoubleOrNull() ?: 69.5,
                            bpMorningSys = bpMornSys.toIntOrNull() ?: 135,
                            bpMorningDia = bpMornDia.toIntOrNull() ?: 82,
                            bpEveningSys = bpEveSys.toIntOrNull() ?: 130,
                            bpEveningDia = bpEveDia.toIntOrNull() ?: 80,
                            temperatureC = temp.toDoubleOrNull() ?: 36.6,
                            bloodSugarG_L = sugar.toDoubleOrNull() ?: 1.30,
                            symptoms = symptoms
                        )
                    )
                },
                modifier = Modifier.testTag("confirm_add_vitals")
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
