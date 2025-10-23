package com.example.docxgenerator.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.docxgenerator.viewmodel.DocumentViewModel

@Composable
fun EditorScreen(documentViewModel: DocumentViewModel, docId: Int) {
    val document by documentViewModel.getDocumentById(docId).collectAsState(initial = null)

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (document != null) {
            Text(text = "Editing document: ${document!!.title}")
        } else {
            Text(text = "Loading document...")
        }
    }
}