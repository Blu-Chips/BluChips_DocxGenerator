package com.example.docxgenerator.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* 
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.* 
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.docxgenerator.database.Document
import com.example.docxgenerator.viewmodel.DocumentViewModel
import com.example.docxgenerator.websocket.WebSocketManager
import com.example.docxgenerator.util.DeviceHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentListScreen(
    documentViewModel: DocumentViewModel,
    onDocumentClick: (Int) -> Unit,
    onNewDocumentClick: () -> Unit
) {
    val documents by documentViewModel.allDocuments.collectAsState(initial = emptyList())
    val context = LocalContext.current
    val webSocketManager = WebSocketManager.getInstance(context)
    
    val isServerRunning by webSocketManager.isRunning.collectAsState()
    val clientCount by webSocketManager.clientCount.collectAsState()
    val serverIp by webSocketManager.serverIp.collectAsState()
    val serverPort by webSocketManager.serverPort.collectAsState()

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
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            // Emulator Info Card (only show on emulators)
            if (DeviceHelper.isEmulator() && !DeviceHelper.isArmArchitecture()) {
                item {
                    EmulatorInfoCard()
                }
            }
            
            // WebSocket Server Status Card
            item {
                WebSocketServerCard(
                    isRunning = isServerRunning,
                    clientCount = clientCount,
                    serverIp = serverIp,
                    serverPort = serverPort,
                    onToggleServer = {
                        if (isServerRunning) {
                            webSocketManager.stopServer()
                        } else {
                            webSocketManager.startServer()
                        }
                    }
                )
            }
            
            // Document List
            items(documents) { document ->
                DocumentListItem(
                    document = document,
                    onDocumentClick = onDocumentClick
                )
            }
        }
    }
}

@Composable
fun EmulatorInfoCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "ℹ️",
                fontSize = 24.sp,
                modifier = Modifier.padding(end = 12.dp)
            )
            Column {
                Text(
                    text = "Emulator Mode",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Running on x86_64 emulator. All core features work! Note: Native library features (if any) are unavailable on this architecture.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Architecture: ${DeviceHelper.getArchitectureName()}",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun WebSocketServerCard(
    isRunning: Boolean,
    clientCount: Int,
    serverIp: String,
    serverPort: Int,
    onToggleServer: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isRunning) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Share,
                        contentDescription = "WebSocket",
                        tint = if (isRunning) Color(0xFF4CAF50) else Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Collaboration Server",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 20.sp
                        )
                        Text(
                            text = if (isRunning) "Online" else "Offline",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isRunning) Color(0xFF4CAF50) else Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                }
                
                Switch(
                    checked = isRunning,
                    onCheckedChange = { onToggleServer() }
                )
            }
            
            if (isRunning) {
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Server Address:",
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "$serverIp:$serverPort",
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Connected Clients:",
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 16.sp
                        )
                        Text(
                            text = clientCount.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "💡 Other devices can connect to edit documents in real-time",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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