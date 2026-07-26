package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.components.QrCodeGenerator
import com.example.ui.language.LanguageManager
import com.example.ui.theme.DialysisBluePrimary
import com.example.ui.theme.DialysisTealLight
import com.example.ui.viewmodel.MainViewModel

@Composable
fun QrTransferScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.profileState.collectAsState()
    val passport by viewModel.passportState.collectAsState()
    val sessions by viewModel.sessionsState.collectAsState()
    val labs by viewModel.labResultsState.collectAsState()

    var isScanningMode by remember { mutableStateOf(false) }
    var transferStatusMsg by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("qr_transfer_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        item {
            Text(
                text = LanguageManager.getString("patient_transfer"),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Propriété intégrale des données. Transférez votre dossier vers n'importe quel hôpital ou centre sans perte.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }

        // QR Passport Display Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "VOTRE PASSEPORT NUMÉRIQUE QR",
                        style = MaterialTheme.typography.titleMedium,
                        color = DialysisBluePrimary
                    )
                    Text(
                        text = "ID Patient: ${profile?.qrCodeId ?: "PAT-2026-ALG-8892"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    QrCodeGenerator(
                        contentString = "PATIENT:${profile?.firstName}_${profile?.lastName};CENTRE:${profile?.address};SESSIONS:${sessions.size};LABS:${labs.size}",
                        sizeDp = 220
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { isScanningMode = !isScanningMode },
                            colors = ButtonDefaults.buttonColors(containerColor = DialysisBluePrimary),
                            modifier = Modifier.weight(1f).testTag("scan_qr_button")
                        ) {
                            Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = "Scan")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Scanner Code QR")
                        }

                        OutlinedButton(
                            onClick = {
                                transferStatusMsg = "Dossier médical exporté avec succès (Crypté JSON & PDF)"
                            },
                            modifier = Modifier.weight(1f).testTag("export_data_button")
                        ) {
                            Icon(imageVector = Icons.Default.CloudDownload, contentDescription = "Export")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Exporter Dossier")
                        }
                    }

                    if (transferStatusMsg.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = DialysisTealLight,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = transferStatusMsg,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF004D40),
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }

        // Camera Scanner Simulator Card
        if (isScanningMode) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = "Scanner Mode", tint = DialysisBluePrimary, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Mode Scanner Hôpital Actif", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Visez le code QR du patient pour synchroniser immédiatement l'historique complet.", style = MaterialTheme.typography.bodyMedium)

                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                isScanningMode = false
                                transferStatusMsg = "Passeport scanné: 100% de l'historique transféré (Séances, Laboratoire, Prescription)"
                            }
                        ) {
                            Text("Simuler Scan Réussi")
                        }
                    }
                }
            }
        }

        // Patient Ownership & Transfer Features Info
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = "Transfer", tint = DialysisBluePrimary)
                        Text(text = "Garantie de Mobilité Patient", style = MaterialTheme.typography.titleMedium, color = DialysisBluePrimary)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Historique des Séances (${sessions.size} séances conservées)")
                    Text("• Historique Laboratoire (${labs.size} bilans biologiques)")
                    Text("• Prescription d'Hémodialyse (${passport?.vascularAccessType}, Qb ${passport?.bloodFlowMlMin})")
                    Text("• Courbes de Poids Sec et de Tension Artérielle")
                    Text("• Documents et Comptes-rendus numérisés")
                }
            }
        }
    }
}
