package com.example.dmr3a_feedback1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NovelApp()
        }
    }
}

@Composable
fun NovelApp() {
    val novelDatabase = remember {  NovelDatabase.getInstance() }
    var currentScreen by remember { mutableStateOf(Screen.ViewNovels) }

    when (currentScreen) {
        Screen.ViewNovels -> ViewNovelsScreen(novelDatabase) { currentScreen = Screen.AddNovel }
        Screen.AddNovel -> AddNovelScreen(novelDatabase) { currentScreen = Screen.ViewNovels }
    }
}

enum class Screen {
    ViewNovels,
    AddNovel
}

@Composable
fun ViewNovelsScreen(novelDatabase: NovelDatabase, onAddNovelClick: () -> Unit) {
    val novels by remember { mutableStateOf(novelDatabase.getAllNovels()) }
    var refresh by remember { mutableStateOf(false) }

    Column {
        Button(onClick = onAddNovelClick) {
            Text("Añadir Novela")
        }

        novels.mapIndexed { index, novel ->
            key(novel.title) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Título: ${novel.title}")
                        Text("Autor: ${novel.author}")
                        Text("Año: ${novel.year}")
                        Text("Sinopsis: ${novel.synopsis}")
                    }
                    IconButton(onClick = {
                        novelDatabase.removeNovel(novel)
                        refresh = !refresh
                    }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        LaunchedEffect(refresh) {

        }

    }
}

@Composable
fun AddNovelScreen(novelDatabase: NovelDatabase, onNovelAdded: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var synopsis by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título") }
        )
        OutlinedTextField(
            value = author,
            onValueChange = { author = it },
            label = { Text("Autor") }
        )
        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Año") }
        )
        OutlinedTextField(
            value = synopsis,
            onValueChange = { synopsis = it },
            label = { Text("Sinopsis") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val novel = Novel(title, author, year.toIntOrNull() ?: 0, synopsis)
            novelDatabase.addNovel(novel)
            onNovelAdded()
        }) {
            Text("Guardar Novela")
        }
    }
}

