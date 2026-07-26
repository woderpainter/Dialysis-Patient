package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.language.LanguageManager
import com.example.ui.theme.DialysisBluePrimary
import com.example.ui.theme.DialysisTealLight
import com.example.ui.viewmodel.MainViewModel

@Composable
fun SecurityScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val isLocked by viewModel.isLocked.collectAsState()
    val pinCode by viewModel.pinCode.collectAsState()

    var currentPinInput by remember { mutableStateOf("") }
    var newPinInput by remember { mutableStateOf("") }
    var biometricEnabled by remember { mutableStateOf(true) }
    var statusMessage by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("security_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = LanguageManager.getString("security"),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Protection Biométrique, Chiffrement des Données & Conformité RGPD / HIPAA",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }

        // Biometric & Lock Status Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
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
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Shield, contentDescription = "Shield", tint = DialysisBluePrimary, modifier = Modifier.size(32.dp))
                            Column {
                                Text(text = "Statut Sécurité", style = MaterialTheme.typography.titleMedium)
                                Text(text = if (isLocked) "Application Verrouillée" else "Accès Protégé Actif", style = MaterialTheme.typography.bodyMedium)
                            }
                        }

                        Button(
                            onClick = { viewModel.lockApp() },
                            colors = ButtonDefaults.buttonColors(containerColor = DialysisBluePrimary),
                            modifier = Modifier.testTag("lock_app_now_button")
                        ) {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Verrouiller")
                        }
                    }
                }
            }
        }

        // Biometric Login Switch
        item {
            Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Icon(imageVector = Icons.Default.Fingerprint, contentDescription = "Fingerprint", tint = DialysisBluePrimary)
                        Column {
                            Text(text = "Authentification Biométrique", style = MaterialTheme.typography.titleMedium)
                            Text(text = "Empreinte digitale / Reconnaissance faciale", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Switch(
                        checked = biometricEnabled,
                        onCheckedChange = { biometricEnabled = it },
                        modifier = Modifier.testTag("biometric_switch")
                    )
                }
            }
        }

        // Change PIN Code
        item {
            Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Modifier Code PIN de Sécurité", style = MaterialTheme.typography.titleMedium, color = DialysisBluePrimary)

                    OutlinedTextField(
                        value = newPinInput,
                        onValueChange = { newPinInput = it },
                        label = { Text("Nouveau Code PIN à 4 chiffres") },
                        modifier = Modifier.fillMaxWidth().testTag("input_new_pin")
                    )

                    Button(
                        onClick = {
                            if (newPinInput.length >= 4) {
                                viewModel.updatePinCode(newPinInput)
                                statusMessage = "Code PIN mis à jour avec succès!"
                                newPinInput = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth().testTag("save_pin_button")
                    ) {
                        Text("Mettre à jour le Code PIN")
                    }

                    if (statusMessage.isNotBlank()) {
                        Surface(
                            color = DialysisTealLight,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = statusMessage, modifier = Modifier.padding(10.dp), color = Color(0xFF004D40))
                        }
                    }
                }
            }
        }

        // Cloud Security Info
        item {
            Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Sécurité du Cloud & Confidentialité", style = MaterialTheme.typography.titleMedium, color = DialysisBluePrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Chiffrement de bout en bout AES-256")
                    Text("• Conformité RGPD et normes médicales HIPAA")
                    Text("• Sauvegarde automatique dès que la connexion Internet est rétablie")
                    Text("• Fonctionnement Offline-First ininterrompu")
                }
            }
        }
    }
}
