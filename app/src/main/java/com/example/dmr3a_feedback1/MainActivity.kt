package com.example.dmr3a_feedback1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    var currentNovel by remember { mutableStateOf<Novel?>(null) }
    var currentScreen by remember { mutableStateOf(Screen.ViewNovels) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(
                    when (currentScreen) {
                        Screen.ViewNovels -> "Novelas"
                        Screen.AddNovel -> "Añade una novela"
                        Screen.Favorites -> "Favoritos"
                        Screen.NovelDetails -> "Detalles"
                    }
                )
                     },
                navigationIcon = {
                    if (currentScreen == Screen.NovelDetails) {
                        IconButton(onClick = { currentScreen = Screen.ViewNovels }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                },
                actions = {
                    if (currentScreen != Screen.AddNovel) {
                    IconButton(
                        onClick = { currentScreen = Screen.Favorites },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = Color.Red
                        )
                    ) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Favoritos")
                    }

                }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                Screen.ViewNovels -> ViewNovelsScreen(novelDatabase, onAddNovelClick = { currentScreen = Screen.AddNovel }) { novel ->
                    currentNovel = novel
                    currentScreen = Screen.NovelDetails
                }
                Screen.AddNovel -> AddNovelScreen(novelDatabase) { currentScreen = Screen.ViewNovels }
                Screen.Favorites -> FavoritesScreen(novelDatabase) { currentScreen = Screen.ViewNovels }
                Screen.NovelDetails -> if (currentNovel != null) {
                    NovelDetailsScreen(novel = currentNovel!!, novelDatabase = novelDatabase, onBack = { currentScreen = Screen.ViewNovels })
                }
            }
        }
    }
}

enum class Screen {
    ViewNovels,
    AddNovel,
    Favorites,
    NovelDetails
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovelDetailsScreen(novel: Novel, novelDatabase: NovelDatabase, onBack: () -> Unit) {
    var isFavorite by remember { mutableStateOf(novel.getIsFavorite()) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(novel.title) },

            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)) {
            Text("Autor: ${novel.author}", style = MaterialTheme.typography.bodyLarge)
            Text("Año: ${novel.year}", style = MaterialTheme.typography.bodyLarge)
            Text("Sinopsis: ${novel.synopsis}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            IconButton(onClick = {
                novelDatabase.toggleFavorite(novel)
                isFavorite = !isFavorite
            }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                    tint = if (isFavorite) Color.Red else LocalContentColor.current
                )
            }
        }
    }
}

@Composable
fun FavoritesScreen(novelDatabase: NovelDatabase, onBackToHome: () -> Unit) {
    var favoriteNovels by remember { mutableStateOf(novelDatabase.getFavoriteNovels()) }
    var refresh by remember { mutableStateOf(false) }

    Column {

        Button(onClick = onBackToHome) {
            Text("Volver al inicio")
        }

        if (favoriteNovels.isEmpty()) {
            Text("No hay novelas favoritas.")
        } else {
            favoriteNovels.forEach { novel ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Título: ${novel.title}")
                        Text("Autor: ${novel.author}")
                        Text("Año: ${novel.year}")
                        Text("Sinopsis: ${novel.synopsis}")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    IconButton(onClick = {
                        novelDatabase.toggleFavorite(novel)
                        favoriteNovels = novelDatabase.getFavoriteNovels()
                        refresh = !refresh
                    }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar de favoritos")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    LaunchedEffect(refresh) {

    }
}

@Composable
fun ViewNovelsScreen(novelDatabase: NovelDatabase, onAddNovelClick: () -> Unit, onNovelClick: (Novel) -> Unit) {
    val novels by remember { mutableStateOf(novelDatabase.getAllNovels()) }
    var currentNovel: Novel? by remember { mutableStateOf(null) }
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
                    }
                    IconButton(onClick = {
                        novelDatabase.toggleFavorite(novel)
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
                    IconButton(onClick = { onNovelClick(novel) }) {
                        Icon(Icons.Filled.ArrowForward, contentDescription = "Ver detalles")
                    }

                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        if (currentNovel != null) {
            NovelDetailsScreen(
                novel = currentNovel!!,
                novelDatabase = novelDatabase,
                onBack = { currentNovel = null })
        }

    }
        LaunchedEffect(refresh) {

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

