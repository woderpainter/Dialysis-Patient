package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.EmergencyBanner
import com.example.ui.components.QuickActionCard
import com.example.ui.components.SyncBar
import com.example.ui.language.LanguageManager
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.profileState.collectAsState()
    val medicalInfo by viewModel.medicalInfoState.collectAsState()
    val passport by viewModel.passportState.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState()
    val sessions by viewModel.sessionsState.collectAsState()
    val labResults by viewModel.labResultsState.collectAsState()

    val latestSession = sessions.firstOrNull()
    val latestLab = labResults.firstOrNull()


    // Pulse animation for active session status
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MedicalSurfaceLight)
            .padding(horizontal = 16.dp)
            .testTag("home_screen"),
        contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Sync Bar
        item {
            SyncBar(
                isSyncing = isSyncing,
                lastSyncTimestamp = profile?.lastSyncTime ?: System.currentTimeMillis(),
                onSyncNow = { viewModel.triggerCloudSync() }
            )
        }

        // Header greeting & profile Bento header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "MON CARNET NUMÉRIQUE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp,
                            color = DialysisBluePrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Bonjour, ${profile?.firstName ?: "Jean-Pierre"}",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MedicalTextPrimary
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clickable { onNavigate("profile") }
                        .testTag("home_profile_avatar")
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(DialysisBlueLight)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${profile?.firstName?.take(1) ?: "J"}${profile?.lastName?.take(1) ?: "P"}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DialysisBluePrimary
                            )
                        )
                    }
                    // Status dot
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(SuccessGreen)
                            .align(Alignment.BottomEnd)
                    )
                }
            }
        }

        // Emergency Banner Bento
        item {
            EmergencyBanner(onClickEmergency = { onNavigate("emergency") })
        }

        // Prochaine Séance Bento Card (Hero Feature Card)
        item {
            Card(
                onClick = { onNavigate("sessions") },
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = DialysisBluePrimary,
                    contentColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("bento_next_session_card")
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
                        Text(
                            text = "PROCHAINE SÉANCE",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        )
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Demain, 08:30",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF93C5FD).copy(alpha = pulseAlpha))
                        )
                        Text(
                            text = "${medicalInfo?.dialysisCenter ?: "Hôpital Central"} • ${passport?.scheduleTime ?: "08:00 AM - 12:00 PM"}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = Color.White.copy(alpha = 0.95f)
                            )
                        )
                    }
                }
            }
        }

        // Stats Bento Grid Row (2 Columns: Dry Weight + BP)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Bento Card 1: Dry Weight
                Card(
                    onClick = { onNavigate("passport") },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, MedicalCardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(130.dp)
                        .testTag("bento_stat_weight")
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "POIDS SEC",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp,
                                color = MedicalTextSecondary
                            )
                        )

                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "${passport?.dryWeightKg ?: 68.5}",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DialysisBluePrimary
                                )
                            )
                            Text(
                                text = "kg",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MedicalTextSecondary
                                ),
                                modifier = Modifier.padding(bottom = 3.dp)
                            )
                        }

                        Column {
                            LinearProgressIndicator(
                                progress = 0.85f,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(CircleShape),
                                color = DialysisBluePrimary,
                                trackColor = Color(0xFFF1F5F9)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Objectif atteint",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    color = MedicalTextSecondary
                                )
                            )
                        }
                    }
                }

                // Bento Card 2: Tension Artérielle
                Card(
                    onClick = { onNavigate("monitoring") },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, MedicalCardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(130.dp)
                        .testTag("bento_stat_bp")
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "TENSION",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp,
                                color = MedicalTextSecondary
                            )
                        )

                        Text(
                            text = "${latestSession?.bpAfterSys ?: 120}/${latestSession?.bpAfterDia ?: 80}",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MedicalTextPrimary
                            )
                        )

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SuccessGreenContainer,
                            modifier = Modifier.wrapContentWidth()
                        ) {
                            Text(
                                text = "STABLE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 11.sp,
                                    color = SuccessGreen
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // AI Assistant Bento Banner Card
        item {
            Card(
                onClick = { onNavigate("ai_assistant") },
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = DialysisBlueLight),
                border = BorderStroke(1.dp, BentoBlueBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("bento_ai_assistant")
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = DialysisBluePrimary,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "AI",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ASSISTANT SANTÉ IA",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp,
                                color = DialysisBlueDark
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Votre taux de Potassium est stable. Continuez votre régime pauvre en sel.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF1E3A8A)
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Open AI",
                        tint = DialysisBluePrimary
                    )
                }
            }
        }

        // Quick Share Medical Passport QR Bento Card
        item {
            Card(
                onClick = { onNavigate("qr_transfer") },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, MedicalCardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("bento_qr_share")
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
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF8FAFC),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.QrCode2,
                                    contentDescription = "QR Code",
                                    tint = MedicalTextPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "Partager mon Passeport",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MedicalTextPrimary
                                )
                            )
                            Text(
                                text = "Code QR Médical & Transfert d'Hôpital",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MedicalTextSecondary
                                )
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Go",
                        tint = MedicalTextSecondary
                    )
                }
            }
        }

        // Quick Action Bento Modules Header
        item {
            Text(
                text = "MODULES DU CARNET",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = MedicalTextSecondary
                ),
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
        }

        // Quick Action Row 1: Passport & Sessions
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    title = LanguageManager.getString("passport"),
                    subtitle = "Néphrologue, Fistule, Prescription",
                    icon = Icons.Default.MedicalServices,
                    containerColor = Color.White,
                    contentColor = DialysisBluePrimary,
                    onClick = { onNavigate("passport") },
                    modifier = Modifier.weight(1f),
                    testTag = "card_passport"
                )
                QuickActionCard(
                    title = LanguageManager.getString("sessions"),
                    subtitle = "Log séance, Poids & UF",
                    icon = Icons.Default.EventNote,
                    containerColor = Color.White,
                    contentColor = Color(0xFF0F766E),
                    onClick = { onNavigate("sessions") },
                    modifier = Modifier.weight(1f),
                    testTag = "card_sessions"
                )
            }
        }

        // Quick Action Row 2: Vitals & Labs
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    title = LanguageManager.getString("monitoring"),
                    subtitle = "Tension 4x/jour, Poids, Temp",
                    icon = Icons.Default.Favorite,
                    containerColor = Color.White,
                    contentColor = Color(0xFFC2410C),
                    onClick = { onNavigate("monitoring") },
                    modifier = Modifier.weight(1f),
                    testTag = "card_monitoring"
                )
                QuickActionCard(
                    title = LanguageManager.getString("labs"),
                    subtitle = "Potassium, Hb, Créat, Alertes",
                    icon = Icons.Default.Science,
                    containerColor = Color.White,
                    contentColor = Color(0xFF7E22CE),
                    onClick = { onNavigate("labs") },
                    modifier = Modifier.weight(1f),
                    testTag = "card_labs"
                )
            }
        }

        // Quick Action Row 3: Nutrition & Documents
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    title = LanguageManager.getString("nutrition"),
                    subtitle = "Calculateur eau, Guide potassium",
                    icon = Icons.Default.WaterDrop,
                    containerColor = Color.White,
                    contentColor = Color(0xFF15803D),
                    onClick = { onNavigate("nutrition") },
                    modifier = Modifier.weight(1f),
                    testTag = "card_nutrition"
                )
                QuickActionCard(
                    title = LanguageManager.getString("docs"),
                    subtitle = "Scanner PDF & Radios",
                    icon = Icons.Default.FolderSpecial,
                    containerColor = Color.White,
                    contentColor = Color(0xFF334155),
                    onClick = { onNavigate("docs") },
                    modifier = Modifier.weight(1f),
                    testTag = "card_docs"
                )
            }
        }

        // Recent Alert / Insights Preview Card (Bento Style)
        item {
            latestLab?.let { lab ->
                if (lab.potassiumMeq_L > 5.5 || lab.hemoglobinG_dL < 10.0 || lab.phosphorusMg_dL > 5.5) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = EmergencyRedContainer
                        ),
                        border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ReportProblem,
                                contentDescription = "Alert",
                                tint = EmergencyRed,
                                modifier = Modifier.size(32.dp)
                            )
                            Column {
                                Text(
                                    text = "Alerte Laboratoire (${lab.dateString})",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = EmergencyRed
                                )
                                if (lab.potassiumMeq_L > 5.5) {
                                    Text(
                                        text = "• Potassium Élevé: ${lab.potassiumMeq_L} meq/L",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = EmergencyRed
                                    )
                                }
                                if (lab.hemoglobinG_dL < 10.0) {
                                    Text(
                                        text = "• Anémie: Hémoglobine ${lab.hemoglobinG_dL} g/dL",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = EmergencyRed
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

