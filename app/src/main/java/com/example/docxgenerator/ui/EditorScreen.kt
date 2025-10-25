package com.example.docxgenerator.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.docxgenerator.viewmodel.DocumentViewModel

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Edit Document",
                        style = MaterialTheme.typography.headlineMedium
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
    Column(
        modifier = Modifier
            .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
                label = { Text("Document Title", fontSize = 18.sp) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.headlineSmall
        )
            Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
                label = { Text("Document Content", fontSize = 18.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f),
                maxLines = Int.MAX_VALUE,
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
        )
            Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                document?.let {
                    val updatedDocument = it.copy(title = title, content = content)
                    documentViewModel.update(updatedDocument)
                }
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
        ) {
                Text(
                    text = "SAVE DOCUMENT",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}