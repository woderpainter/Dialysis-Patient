package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.QrCodeGenerator
import com.example.ui.language.LanguageManager
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.EmergencyRedContainer
import com.example.ui.viewmodel.MainViewModel

@Composable
fun EmergencyCardScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.profileState.collectAsState()
    val medical by viewModel.medicalInfoState.collectAsState()
    val passport by viewModel.passportState.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(EmergencyRedContainer)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("emergency_card_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // High Visibility Red Top Badge
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = EmergencyRed),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Alert",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = LanguageManager.getString("emergency_card_title"),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = LanguageManager.getString("dialysis_patient_badge"),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Yellow
                )
            }
        }

        // Patient Core Info Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "NOM: ${profile?.firstName?.uppercase()} ${profile?.lastName?.uppercase()}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black
                )
                Text(
                    text = "GROUPE SANGUIN: ${profile?.bloodGroup}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = EmergencyRed,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "ÂGE / NE: ${profile?.dateOfBirth} (${profile?.age} ans)",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.DarkGray
                )
            }
        }

        // Critical Access & Allergy Warnings
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "⚠️ ACCÈS VASCULAIRE - CONSIGNES STRICTES:",
                    style = MaterialTheme.typography.titleLarge,
                    color = EmergencyRed,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "• Type: ${passport?.vascularAccessType} (${passport?.accessLocation})",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                Text(
                    text = "• INTERDICTION STRICTE DE PRISE DE TENSION / PERFUSION SUR LE BRAS À FISTULE!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = EmergencyRed,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))
                Divider()
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "ALLERGIES CONNUES:",
                    style = MaterialTheme.typography.titleMedium,
                    color = EmergencyRed
                )
                Text(
                    text = medical?.allergies?.ifBlank { "Aucune allergie signalée" } ?: "Pénicilline, Produit de contraste",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
            }
        }

        // Emergency Contacts & Buttons
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "CONTACTS D'URGENCE:",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )

                Text(
                    text = "• Proche d'urgence: ${profile?.emergencyContact}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "• Néphrologue: ${medical?.treatingNephrologist}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "• Centre de Dialyse: ${medical?.dialysisCenter}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { /* Call action */ },
                        colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("call_emergency_button")
                    ) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = "Call")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Appeler Secours")
                    }

                    Button(
                        onClick = { /* Call Nephrologist */ },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Icon(imageVector = Icons.Default.LocalHospital, contentDescription = "Hospital")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Appeler Néphro")
                    }
                }
            }
        }

        // Emergency QR Code Passport Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "CODE QR PASSEPORT DIALYSE",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                Text(
                    text = "Chaque hôpital/SAMU peut scanner ce QR pour accéder à la prescription",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(12.dp))

                QrCodeGenerator(
                    contentString = "PATIENT_ID:${profile?.qrCodeId};NAME:${profile?.firstName}_${profile?.lastName};BLOOD:${profile?.bloodGroup};DRY_WEIGHT:${passport?.dryWeightKg}",
                    sizeDp = 180
                )
            }
        }
    }
}
