package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.LabResultEntity
import com.example.ui.components.ChartCanvas
import com.example.ui.components.ChartPoint
import com.example.ui.language.LanguageManager
import com.example.ui.theme.DialysisBluePrimary
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.EmergencyRedContainer
import com.example.ui.viewmodel.MainViewModel

@Composable
fun LabResultsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val labs by viewModel.labResultsState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    // Potassium Trend
    val potassiumChartPoints = labs.reversed().map {
        ChartPoint(label = it.dateString.takeLast(5), value = it.potassiumMeq_L.toFloat())
    }

    // Hemoglobin Trend
    val hbChartPoints = labs.reversed().map {
        ChartPoint(label = it.dateString.takeLast(5), value = it.hemoglobinG_dL.toFloat())
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add Lab") },
                text = { Text(LanguageManager.getString("add_lab_result")) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.testTag("add_lab_fab")
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .testTag("labs_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
        ) {
            item {
                Text(
                    text = LanguageManager.getString("lab_history"),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Suivi biologique complet avec détection automatique d'anomalies",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }

            // Potassium Chart
            item {
                ChartCanvas(
                    title = "Évolution Potassium (K+) - Seuil Alerte > 5.5 meq/L",
                    points = potassiumChartPoints,
                    targetValue = 5.5f,
                    targetLabel = "Alerte Max",
                    unit = "meq/L",
                    lineColor = EmergencyRed
                )
            }

            // Hemoglobin Chart
            item {
                ChartCanvas(
                    title = "Évolution Hémoglobine (Hb g/dL) - Cible 10-12 g/dL",
                    points = hbChartPoints,
                    targetValue = 10.0f,
                    targetLabel = "Seuil Min",
                    unit = "g/dL",
                    lineColor = DialysisBluePrimary
                )
            }

            item {
                Text(
                    text = "Résultats des Bilans Mensuels",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            items(labs) { lab ->
                LabResultCard(lab = lab)
            }
        }
    }

    if (showAddDialog) {
        AddLabResultDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { newLab ->
                viewModel.addLabResult(newLab)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun LabResultCard(lab: LabResultEntity) {
    val isPotassiumHigh = lab.potassiumMeq_L > 5.5
    val isHbLow = lab.hemoglobinG_dL < 10.0
    val isPhosHigh = lab.phosphorusMg_dL > 5.5

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPotassiumHigh || isHbLow || isPhosHigh) EmergencyRedContainer else MaterialTheme.colorScheme.surface
        ),
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
                    Icon(imageVector = Icons.Default.Science, contentDescription = "Lab", tint = DialysisBluePrimary)
                    Text(text = "Bilan du ${lab.dateString}", style = MaterialTheme.typography.titleMedium)
                }
                if (isPotassiumHigh || isHbLow || isPhosHigh) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = "Alert", tint = EmergencyRed)
                }
            }

            if (isPotassiumHigh) {
                Text(text = "⚠️ ALERTE HYPERKALÉMIE: Potassium ${lab.potassiumMeq_L} meq/L", style = MaterialTheme.typography.bodyMedium, color = EmergencyRed)
            }
            if (isHbLow) {
                Text(text = "⚠️ ALERTE ANÉMIE: Hémoglobine ${lab.hemoglobinG_dL} g/dL", style = MaterialTheme.typography.bodyMedium, color = EmergencyRed)
            }
            if (isPhosHigh) {
                Text(text = "⚠️ ALERTE HYPERPHOSPHATÉMIE: Phosphore ${lab.phosphorusMg_dL} mg/dL", style = MaterialTheme.typography.bodyMedium, color = EmergencyRed)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))

            // Grid of parameters
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Potassium (K+): ${lab.potassiumMeq_L}", style = MaterialTheme.typography.bodyMedium)
                    Text("Hémoglobine: ${lab.hemoglobinG_dL}", style = MaterialTheme.typography.bodyMedium)
                    Text("Créatinine: ${lab.creatinineMg_dL}", style = MaterialTheme.typography.bodyMedium)
                    Text("Urée: ${lab.ureaG_L}", style = MaterialTheme.typography.bodyMedium)
                }
                Column {
                    Text("Phosphore: ${lab.phosphorusMg_dL}", style = MaterialTheme.typography.bodyMedium)
                    Text("Calcium: ${lab.calciumMg_dL}", style = MaterialTheme.typography.bodyMedium)
                    Text("Albumine: ${lab.albuminG_L}", style = MaterialTheme.typography.bodyMedium)
                    Text("Ferritine: ${lab.ferritinNg_mL}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
private fun AddLabResultDialog(
    onDismiss: () -> Unit,
    onConfirm: (LabResultEntity) -> Unit
) {
    var hb by remember { mutableStateOf("11.0") }
    var pot by remember { mutableStateOf("5.2") }
    var creat by remember { mutableStateOf("8.5") }
    var urea by remember { mutableStateOf("1.4") }
    var phos by remember { mutableStateOf("5.2") }
    var calc by remember { mutableStateOf("9.1") }
    var alb by remember { mutableStateOf("38.0") }
    var ferr by remember { mutableStateOf("400.0") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ajouter Bilan Biologique") },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = pot, onValueChange = { pot = it }, label = { Text("Potassium (K+)") }, modifier = Modifier.weight(1f).testTag("input_potassium"))
                        OutlinedTextField(value = hb, onValueChange = { hb = it }, label = { Text("Hémoglobine (Hb)") }, modifier = Modifier.weight(1f).testTag("input_hb"))
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = creat, onValueChange = { creat = it }, label = { Text("Créatinine") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = urea, onValueChange = { urea = it }, label = { Text("Urée") }, modifier = Modifier.weight(1f))
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = phos, onValueChange = { phos = it }, label = { Text("Phosphore") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = calc, onValueChange = { calc = it }, label = { Text("Calcium") }, modifier = Modifier.weight(1f))
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = alb, onValueChange = { alb = it }, label = { Text("Albumine") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = ferr, onValueChange = { ferr = it }, label = { Text("Ferritine") }, modifier = Modifier.weight(1f))
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        LabResultEntity(
                            potassiumMeq_L = pot.toDoubleOrNull() ?: 5.2,
                            hemoglobinG_dL = hb.toDoubleOrNull() ?: 11.0,
                            creatinineMg_dL = creat.toDoubleOrNull() ?: 8.5,
                            ureaG_L = urea.toDoubleOrNull() ?: 1.4,
                            phosphorusMg_dL = phos.toDoubleOrNull() ?: 5.2,
                            calciumMg_dL = calc.toDoubleOrNull() ?: 9.1,
                            albuminG_L = alb.toDoubleOrNull() ?: 38.0,
                            ferritinNg_mL = ferr.toDoubleOrNull() ?: 400.0
                        )
                    )
                },
                modifier = Modifier.testTag("confirm_add_lab")
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
