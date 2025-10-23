package com.example.docxgenerator.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.docxgenerator.viewmodel.DocumentViewModel

@Composable
fun EditorScreen(documentViewModel: DocumentViewModel, docId: Int) {
    val document by documentViewModel.getDocumentById(docId).collectAsState(initial = null)
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    LaunchedEffect(document) {
        document?.let {
            title = it.title
            content = it.content
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Document Title") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Quill Editor Placeholder")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                document?.let {
                    val updatedDocument = it.copy(title = title, content = content)
                    documentViewModel.update(updatedDocument)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Save")
        }
    }
}