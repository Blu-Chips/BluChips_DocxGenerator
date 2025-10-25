package com.example.docxgenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.docxgenerator.database.Document
import com.example.docxgenerator.database.DocumentDatabase
import com.example.docxgenerator.ui.DocumentListScreen
import com.example.docxgenerator.ui.EditorScreen
import com.example.docxgenerator.ui.theme.DocxGeneratorTheme
import com.example.docxgenerator.viewmodel.DocumentViewModel
import com.example.docxgenerator.viewmodel.DocumentViewModelFactory

class MainActivity : ComponentActivity() {

    private val documentViewModel: DocumentViewModel by viewModels {
        DocumentViewModelFactory(DocumentDatabase.getDatabase(this).documentDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DocxGeneratorTheme {
                NavGraph(documentViewModel = documentViewModel)
            }
        }
    }
}

@Composable
fun NavGraph(
    documentViewModel: DocumentViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "documentList") {
        composable("documentList") {
            DocumentListScreen(
                documentViewModel = documentViewModel,
                onDocumentClick = { docId ->
                    navController.navigate("editor/$docId")
                },
                onNewDocumentClick = {
                    val newDoc = Document(title = "New Document", content = "")
                    documentViewModel.insert(newDoc)
                }
            )
        }
        composable(
            route = "editor/{docId}",
            arguments = listOf(navArgument("docId") { type = NavType.IntType })
        ) {
            backStackEntry ->
            val docId = backStackEntry.arguments?.getInt("docId")
            if (docId != null) {
                EditorScreen(
                    documentViewModel = documentViewModel,
                    docId = docId,
                    onClose = { navController.popBackStack() }
                )
            }
        }
    }
}