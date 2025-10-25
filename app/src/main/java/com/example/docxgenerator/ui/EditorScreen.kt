package com.example.docxgenerator.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.docxgenerator.viewmodel.DocumentViewModel
import com.example.docxgenerator.websocket.WebSocketManager
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    documentViewModel: DocumentViewModel, 
    docId: Int,
    onClose: () -> Unit
) {
    val document by documentViewModel.getDocumentById(docId).collectAsState(initial = null)
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    
    val context = LocalContext.current
    val webSocketManager = WebSocketManager.getInstance(context)
    val isServerRunning by webSocketManager.isRunning.collectAsState()
    val clientCount by webSocketManager.clientCount.collectAsState()
    
    // Auto-save and broadcast with debouncing
    LaunchedEffect(title, content) {
        if (title.isNotEmpty() || content.isNotEmpty()) {
            delay(1000) // Wait 1 second after user stops typing
            document?.let {
                val updatedDocument = it.copy(title = title, content = content)
                documentViewModel.update(updatedDocument)
                
                // Broadcast to connected clients if server is running
                if (isServerRunning) {
                    webSocketManager.broadcastDocumentUpdate(docId, title, content)
                }
            }
        }
    }

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
                    Column {
                        Text(
                            "Edit Document",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        if (isServerRunning && clientCount > 0) {
                            Text(
                                "🔄 Syncing with $clientCount ${if (clientCount == 1) "device" else "devices"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF4CAF50)
                            )
                        }
                    }
                },
                actions = {
                    if (isServerRunning) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                Icons.Filled.Share,
                                contentDescription = "Sharing",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$clientCount",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF4CAF50)
                            )
                        }
                    }
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
            
            // Save and Close Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Save Button
        Button(
            onClick = {
                document?.let {
                    val updatedDocument = it.copy(title = title, content = content)
                    documentViewModel.update(updatedDocument)
                            
                            // Broadcast if server is running
                            if (isServerRunning) {
                                webSocketManager.broadcastDocumentUpdate(docId, title, content)
                            }
                }
            },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "SAVE",
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                
                // Save and Close Button
                Button(
                    onClick = {
                        document?.let {
                            val updatedDocument = it.copy(title = title, content = content)
                            documentViewModel.update(updatedDocument)
                            
                            // Broadcast if server is running
                            if (isServerRunning) {
                                webSocketManager.broadcastDocumentUpdate(docId, title, content)
                            }
                        }
                        onClose()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
        ) {
                    Text(
                        text = "SAVE & CLOSE",
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}