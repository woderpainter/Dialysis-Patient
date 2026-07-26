package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Send
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
fun AiAssistantScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val aiResponse by viewModel.aiResponse.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()

    var userQuery by remember { mutableStateOf("") }

    val quickQuestions = listOf(
        "Explique mes derniers résultats de laboratoire",
        "Avertissement sur le potassium élevé et aliments autorisés",
        "Quelle est ma prise de poids idéale entre deux séances?",
        "Conseils pour respecter la restriction en eau"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("ai_assistant_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // AI Title Banner
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "AI",
                    tint = DialysisBluePrimary,
                    modifier = Modifier.size(36.dp)
                )
                Column {
                    Text(
                        text = LanguageManager.getString("ai_assistant"),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Explication simplifiée des bilans & Conseils d'hygiène de vie",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Quick Suggestion Chips
        Text(text = "Questions Fréquentes:", style = MaterialTheme.typography.labelLarge)
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    quickQuestions.forEach { question ->
                        OutlinedCard(
                            onClick = {
                                userQuery = question
                                viewModel.askAiAssistant(question)
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().testTag("chip_ai_question")
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Ask",
                                    tint = DialysisBluePrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = question,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }

            // Response Box
            item {
                if (isAiLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (aiResponse.isNotBlank()) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DialysisTealLight),
                        modifier = Modifier.fillMaxWidth().testTag("ai_response_card")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = "AI", tint = Color(0xFF004D40))
                                Text(
                                    text = "Analyse & Explication Médicale IA:",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(0xFF004D40)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = aiResponse,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF003731)
                            )
                        }
                    }
                }
            }
        }

        // User Input Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = userQuery,
                onValueChange = { userQuery = it },
                placeholder = { Text("Posez votre question à l'assistant...") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_ai_query")
            )
            IconButton(
                onClick = {
                    if (userQuery.isNotBlank()) {
                        viewModel.askAiAssistant(userQuery)
                    }
                },
                modifier = Modifier.testTag("send_ai_query_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = DialysisBluePrimary
                )
            }
        }
    }
}
