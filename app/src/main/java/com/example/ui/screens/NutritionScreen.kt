package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NutritionEntryEntity
import com.example.ui.language.LanguageManager
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NutritionScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val nutritionLogs by viewModel.nutritionState.collectAsState()
    val passport by viewModel.passportState.collectAsState()

    var selectedCategory by remember { mutableStateOf("ALL") } // "ALL", "POTASSIUM", "PHOSPHORUS", "SALT"
    var showAddFluidDialog by remember { mutableStateOf(false) }

    var urineOutputMl by remember { mutableStateOf("200") }
    val calculatedFluidLimit = 500 + (urineOutputMl.toIntOrNull() ?: 200)
    val todayFluidIntake = nutritionLogs.firstOrNull()?.fluidIntakeMl ?: 650

    val progress = (todayFluidIntake.toFloat() / calculatedFluidLimit.toFloat()).coerceIn(0f, 1f)
    val isOverLimit = todayFluidIntake > calculatedFluidLimit

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MedicalSurfaceLight)
            .padding(horizontal = 16.dp)
            .testTag("nutrition_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp)
    ) {
        // Screen Header
        item {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = "NUTRITION & HYDRATATION",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = DialysisBluePrimary
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = LanguageManager.getString("nutrition"),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MedicalTextPrimary
                    )
                )
                Text(
                    text = "Suivi strict des liquides, potassium, phosphore et sodium",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MedicalTextSecondary
                    )
                )
            }
        }

        // Fluid Intake Bento Hero Card
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = DialysisBluePrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.WaterDrop,
                                        contentDescription = "Fluid",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Text(
                                text = "APPORT HYDRIQUE",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isOverLimit) EmergencyRed else SuccessGreenContainer
                        ) {
                            Text(
                                text = if (isOverLimit) "ALERTE DÉPASSEMENT" else "DANS LES LIMITES",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 11.sp,
                                    color = if (isOverLimit) Color.White else SuccessGreen
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "Consommé aujourd'hui",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            )
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "$todayFluidIntake",
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 38.sp,
                                        color = Color.White
                                    )
                                )
                                Text(
                                    text = " / $calculatedFluidLimit mL",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White.copy(alpha = 0.85f)
                                    ),
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                            }
                        }

                        Button(
                            onClick = { showAddFluidDialog = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = DialysisBluePrimary
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.testTag("add_fluid_button")
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ajouter Eau", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Fluid Progress Bar
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(CircleShape),
                        color = if (isOverLimit) EmergencyRed else Color(0xFF60A5FA),
                        trackColor = Color.White.copy(alpha = 0.25f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Quick Fluid Add Badges Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(100, 150, 250).forEach { amount ->
                            Surface(
                                onClick = {
                                    viewModel.addNutritionEntry(
                                        NutritionEntryEntity(
                                            fluidIntakeMl = todayFluidIntake + amount,
                                            foodLog = "Consommation eau: +$amount mL"
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.18f),
                                contentColor = Color.White,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = "+$amount mL",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Fluid Restriction Calculator Bento Row
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, MedicalCardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = DialysisBlueLight,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Calculate,
                                    contentDescription = "Calc",
                                    tint = DialysisBluePrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = LanguageManager.getString("fluid_calculator"),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MedicalTextPrimary
                                )
                            )
                            Text(
                                text = "Formule: 500 mL + Diurèse résiduelle 24h",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MedicalTextSecondary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = urineOutputMl,
                        onValueChange = { urineOutputMl = it },
                        label = { Text("Diurèse Résiduelle (mL d'urine / 24h)") },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_urine_output")
                    )
                }
            }
        }

        // Category Filter Chips
        item {
            Column(modifier = Modifier.padding(top = 4.dp)) {
                Text(
                    text = "GUIDES ALIMENTAIRES",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = MedicalTextSecondary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FilterChip(
                        selected = selectedCategory == "ALL",
                        onClick = { selectedCategory = "ALL" },
                        label = { Text("Tout Voir") },
                        shape = RoundedCornerShape(12.dp)
                    )
                    FilterChip(
                        selected = selectedCategory == "POTASSIUM",
                        onClick = { selectedCategory = "POTASSIUM" },
                        label = { Text("Potassium") },
                        shape = RoundedCornerShape(12.dp)
                    )
                    FilterChip(
                        selected = selectedCategory == "PHOSPHORUS",
                        onClick = { selectedCategory = "PHOSPHORUS" },
                        label = { Text("Phosphore") },
                        shape = RoundedCornerShape(12.dp)
                    )
                    FilterChip(
                        selected = selectedCategory == "SALT",
                        onClick = { selectedCategory = "SALT" },
                        label = { Text("Sel & Sodium") },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        // Bento Food Cards Grid Content
        if (selectedCategory == "ALL" || selectedCategory == "POTASSIUM") {
            item {
                BentoPotassiumCard()
            }
        }

        if (selectedCategory == "ALL" || selectedCategory == "PHOSPHORUS") {
            item {
                BentoPhosphorusCard()
            }
        }

        if (selectedCategory == "ALL" || selectedCategory == "SALT") {
            item {
                BentoSaltCard()
            }
        }
    }

    if (showAddFluidDialog) {
        AddFluidDialog(
            onDismiss = { showAddFluidDialog = false },
            onConfirm = { amountMl ->
                viewModel.addNutritionEntry(
                    NutritionEntryEntity(
                        fluidIntakeMl = todayFluidIntake + amountMl,
                        foodLog = "Consommation eau / boisson: +$amountMl mL"
                    )
                )
                showAddFluidDialog = false
            }
        )
    }
}

@Composable
private fun BentoPotassiumCard() {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, MedicalCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFDCFCE7),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Eco,
                            contentDescription = "Potassium",
                            tint = Color(0xFF16A34A),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Column {
                    Text(
                        text = "Guide Potassium (K+)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MedicalTextPrimary
                        )
                    )
                    Text(
                        text = "Cible: Maintenir K+ entre 3.5 et 5.2 meq/L",
                        style = MaterialTheme.typography.bodySmall.copy(color = MedicalTextSecondary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Recommended vs Avoid Split Box
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Allowed Foods Box
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF0FDF4),
                    border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(16.dp))
                            Text(
                                text = "Recommandés",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("• Pommes & Poires\n• Fraises & Myrtilles\n• Concombres & Oignons\n• Pâtes & Riz Blanc", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 16.sp, color = MedicalTextPrimary))
                    }
                }

                // Forbidden Foods Box
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFEF2F2),
                    border = BorderStroke(1.dp, Color(0xFFFECACA)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(imageVector = Icons.Default.Cancel, contentDescription = null, tint = EmergencyRed, modifier = Modifier.size(16.dp))
                            Text(
                                text = "À Éviter (Riches K+)",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EmergencyRed
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("• Bananes, Dattes, Figues\n• Pommes de terre\n• Chocolat & Cacao\n• Sauce tomate concentrée", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 16.sp, color = MedicalTextPrimary))
                    }
                }
            }
        }
    }
}

@Composable
private fun BentoPhosphorusCard() {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, MedicalCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFEF3C7),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Medication,
                            contentDescription = "Phosphorus",
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Column {
                    Text(
                        text = "Guide Phosphore & Chélateurs",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MedicalTextPrimary
                        )
                    )
                    Text(
                        text = "Protection osseuse & vasculaire",
                        style = MaterialTheme.typography.bodySmall.copy(color = MedicalTextSecondary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Renagel Banner
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFFFFBEB),
                border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Rappel crucial: Prenez vos chélateurs (Renagel / Calperos) STRICTEMENT au milieu des repas!",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF92400E)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "❌ Aliments à fort apport en phosphore:",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = EmergencyRed)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "• Fromages fondus et à pâte dure • Lait concentré\n• Boissons au cola & sodas industriels • Charcuteries avec additifs E338-E343",
                style = MaterialTheme.typography.bodySmall.copy(color = MedicalTextPrimary)
            )
        }
    }
}

@Composable
private fun BentoSaltCard() {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, MedicalCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DialysisBlueLight,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Restaurant,
                            contentDescription = "Salt",
                            tint = DialysisBluePrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Column {
                    Text(
                        text = "Guide Sel & Sodium (< 4g / jour)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MedicalTextPrimary
                        )
                    )
                    Text(
                        text = "Contrôle de la soif et de la tension artérielle",
                        style = MaterialTheme.typography.bodySmall.copy(color = MedicalTextSecondary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF1F5F9),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "💡 Astuce contre la soif:",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DialysisBluePrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "• Le sel est la cause principale de la sensation de soif entre les séances.\n• Assaisonnez vos plats avec des herbes aromatiques, de l'ail et du citron au lieu du sel de table.\n• Évitez impérativement les cubes de bouillon concentrés.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MedicalTextPrimary)
                    )
                }
            }
        }
    }
}

@Composable
private fun AddFluidDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var amount by remember { mutableStateOf("150") }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = "Ajouter Boisson (mL)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column {
                Text(
                    text = "Entrez le volume consommé (eau, thé, soupe, café...):",
                    style = MaterialTheme.typography.bodySmall.copy(color = MedicalTextSecondary)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Volume en mL") },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_fluid_amount")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val ml = amount.toIntOrNull() ?: 150
                    onConfirm(ml)
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("confirm_add_fluid")
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
