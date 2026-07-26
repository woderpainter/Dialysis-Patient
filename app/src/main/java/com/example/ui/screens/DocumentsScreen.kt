package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.MedicalDocumentEntity
import com.example.ui.language.LanguageManager
import com.example.ui.theme.DialysisBluePrimary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun DocumentsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val documents by viewModel.documentsState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add Doc") },
                text = { Text("Scanner / Ajouter Document") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.testTag("add_doc_fab")
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .testTag("documents_screen")
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = LanguageManager.getString("docs"),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Comptes-rendus, PDF laboratoires, Ordonnances & Échographies",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (documents.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Aucun document médical enregistré.")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(documents) { doc ->
                        DocumentCard(document = doc)
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddDocumentDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { newDoc ->
                viewModel.addDocument(newDoc)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun DocumentCard(document: MedicalDocumentEntity) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
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
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DialysisBluePrimary.copy(alpha = 0.1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.PictureAsPdf,
                        contentDescription = "PDF",
                        tint = DialysisBluePrimary,
                        modifier = Modifier.padding(10.dp).size(28.dp)
                    )
                }
                Column {
                    Text(
                        text = document.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Catégorie: ${document.category} • ${document.dateString}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (document.summary.isNotBlank()) {
                        Text(
                            text = document.summary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            TextButton(onClick = { /* Simulated View */ }) {
                Text("Ouvrir")
            }
        }
    }
}

@Composable
private fun AddDocumentDialog(
    onDismiss: () -> Unit,
    onConfirm: (MedicalDocumentEntity) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Radiologie") }
    var summary by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Numériser / Importer Document") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Titre du document (ex: Écho Doppler Fistule)") },
                    modifier = Modifier.fillMaxWidth().testTag("input_doc_title")
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Catégorie (Radiologie, Ordonnance, Bilan PDF)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = summary,
                    onValueChange = { summary = it },
                    label = { Text("Résumé / Notes médicales") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(
                            MedicalDocumentEntity(
                                title = title,
                                category = category,
                                summary = summary
                            )
                        )
                    }
                },
                modifier = Modifier.testTag("confirm_add_doc")
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
