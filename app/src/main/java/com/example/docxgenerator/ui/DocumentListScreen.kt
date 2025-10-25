package com.example.docxgenerator.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* 
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.* 
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.docxgenerator.database.Document
import com.example.docxgenerator.viewmodel.DocumentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentListScreen(
    documentViewModel: DocumentViewModel,
    onDocumentClick: (Int) -> Unit,
    onNewDocumentClick: () -> Unit
) {
    val documents by documentViewModel.allDocuments.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "BluChips Documents",
                        style = MaterialTheme.typography.headlineMedium
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNewDocumentClick,
                icon = { Icon(Icons.Filled.Add, contentDescription = "New Document") },
                text = { Text("New Document", style = MaterialTheme.typography.titleMedium) }
            )
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(documents) {
                document -> DocumentListItem(document = document, onDocumentClick = onDocumentClick)
            }
        }
    }
}

@Composable
fun DocumentListItem(document: Document, onDocumentClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { onDocumentClick(document.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
    ) {
        Text(
            text = document.title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = document.content.take(100),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
        )
        }
    }
}