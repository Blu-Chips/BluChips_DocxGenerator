package com.example.docxgenerator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docxgenerator.database.Document
import com.example.docxgenerator.database.DocumentDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DocumentViewModel(private val documentDao: DocumentDao) : ViewModel() {

    val allDocuments: Flow<List<Document>> = documentDao.getAllDocuments()

    fun getDocumentById(id: Int): Flow<Document> {
        return documentDao.getDocumentById(id)
    }

    fun insert(document: Document) {
        viewModelScope.launch {
            documentDao.insert(document)
        }
    }

    fun update(document: Document) {
        viewModelScope.launch {
            documentDao.update(document)
        }
    }

    fun delete(document: Document) {
        viewModelScope.launch {
            documentDao.delete(document)
        }
    }
}