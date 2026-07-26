package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.language.LanguageManager
import com.example.ui.theme.EmergencyRed

@Composable
fun EmergencyBanner(
    onClickEmergency: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClickEmergency,
        colors = CardDefaults.cardColors(
            containerColor = EmergencyRed,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("emergency_banner")
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "!",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 22.sp,
                                color = Color.White
                            )
                        )
                    }
                }
                Column {
                    Text(
                        text = "URGENCE MÉDICALE",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    )
                    Text(
                        text = LanguageManager.getString("emergency_card_title"),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Open Emergency",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

