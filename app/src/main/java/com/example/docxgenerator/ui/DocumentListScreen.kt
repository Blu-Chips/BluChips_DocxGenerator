package com.example.docxgenerator.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* 
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.* 
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.docxgenerator.database.Document
import com.example.docxgenerator.viewmodel.DocumentViewModel

@Composable
fun DocumentListScreen(
    documentViewModel: DocumentViewModel,
    onDocumentClick: (Int) -> Unit,
    onNewDocumentClick: () -> Unit
) {
    val documents by documentViewModel.allDocuments.collectAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNewDocumentClick) {
                Icon(Icons.Filled.Add, contentDescription = "New Document")
            }
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
            .padding(8.dp)
            .clickable { onDocumentClick(document.id) },
        elevation = 4.dp
    ) {
        Text(
            text = document.title,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.h6
        )
    }
}