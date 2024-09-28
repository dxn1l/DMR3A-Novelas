package com.example.dmr3a_feedback1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NovelApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovelApp() {
       val novelDatabase = remember { NovelDatabase.getInstance() }
    var currentScreen by remember { mutableStateOf(Screen.ViewNovels) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (currentScreen == Screen.Favorites) "Favoritos" else "Novelas") },
                actions = {
                    IconButton(
                        onClick = { currentScreen = Screen.Favorites },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = Color.Red
                        )
                        ) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Favoritos")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                Screen.ViewNovels -> ViewNovelsScreen(novelDatabase) { currentScreen = Screen.AddNovel }
                Screen.AddNovel -> AddNovelScreen(novelDatabase) { currentScreen = Screen.ViewNovels }
                Screen.Favorites -> FavoritesScreen(novelDatabase) { currentScreen = Screen.ViewNovels }
            }
        }
    }
}

enum class Screen {
    ViewNovels,
    AddNovel,
    Favorites
}

@Composable
fun FavoritesScreen(novelDatabase: NovelDatabase, onBackToHome: () -> Unit) {
    val favoriteNovels = novelDatabase.getFavoriteNovels()

    Column {

        Button(onClick = onBackToHome) { // Agregar botón
            Text("Volver al inicio")
        }

        if (favoriteNovels.isEmpty()) {
            Text("No hay novelas favoritas.")
        } else {
            favoriteNovels.forEach { novel ->
                Text("Título: ${novel.title}")
                Text("Autor: ${novel.author}")
                Text("Año: ${novel.year}")
                Text("Sinopsis: ${novel.synopsis}")
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
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
                    IconButton(onClick = { novelDatabase.toggleFavorite(novel)
                        refresh = !refresh
                    }) {
                        Icon(
                            imageVector = if (novel.getIsFavorite()) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (novel.getIsFavorite()) "Quitar de favoritos" else "Agregar a favoritos",
                            tint = if (novel.getIsFavorite()) Color.Red else Color.Gray
                        )
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

