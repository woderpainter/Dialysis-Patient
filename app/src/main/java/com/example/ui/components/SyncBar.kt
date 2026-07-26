package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.SignalWifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.language.LanguageManager
import com.example.ui.theme.DialysisTealLight
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SyncBar(
    isSyncing: Boolean,
    lastSyncTimestamp: Long,
    onSyncNow: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sync_spin")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("sync_bar"),
        color = DialysisTealLight,
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                if (isSyncing) {
                    Icon(
                        imageVector = Icons.Default.CloudSync,
                        contentDescription = "Syncing",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(20.dp)
                            .rotate(angle)
                    )
                    Text(
                        text = "Mise à jour Cloud en cours...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.CloudDone,
                        contentDescription = "Cloud Synced",
                        tint = Color(0xFF00796B),
                        modifier = Modifier.size(20.dp)
                    )
                    Column {
                        Text(
                            text = LanguageManager.getString("synced_badge"),
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFF004D40)
                        )
                        val formattedTime = SimpleDateFormat("HH:mm - dd/MM", Locale.getDefault())
                            .format(Date(lastSyncTimestamp))
                        Text(
                            text = "Synchro: $formattedTime",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF00695C)
                        )
                    }
                }
            }

            Surface(
                onClick = onSyncNow,
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.testTag("sync_now_button")
            ) {
                Text(
                    text = LanguageManager.getString("sync_now"),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}
