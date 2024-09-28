package com.example.dmr3a_feedback1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
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

    MaterialTheme(

        colorScheme = darkColorScheme(
            background = Color.Blue
        )
    ){

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(
                        when (currentScreen) {
                            Screen.ViewNovels -> "Novelas"
                            Screen.AddNovel -> "Añade una novela"
                            Screen.Favorites -> "Favoritos"
                            Screen.NovelDetails -> "Detalles"
                            Screen.AddReview -> "Añadir reseña"
                        }
                    )
                    },
                navigationIcon = {
                    if (currentScreen == Screen.NovelDetails) {
                        IconButton(onClick = { currentScreen = Screen.ViewNovels }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                    else if (currentScreen == Screen.AddNovel) {
                IconButton(onClick = { currentScreen = Screen.ViewNovels }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                }
            }else if (currentScreen == Screen.Favorites) {
                        IconButton(onClick = { currentScreen = Screen.ViewNovels }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                    else if (currentScreen == Screen.AddReview) {
                        IconButton(onClick = { currentScreen = Screen.NovelDetails }) {
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
                    Screen.Favorites -> FavoritesScreen(novelDatabase, onBackToHome = { currentScreen = Screen.ViewNovels }, onNovelClick = { novel ->
                        currentNovel = novel
                        currentScreen = Screen.NovelDetails
                    })
                    Screen.NovelDetails -> if (currentNovel != null) {
                        NovelDetailsScreen(
                            novel = currentNovel!!,
                            novelDatabase = novelDatabase,
                            onBack = { currentScreen = Screen.ViewNovels },
                            onAddReviewClick = { currentScreen = Screen.AddReview } // Añadir onAddReviewClick
                        )
                    }
                    Screen.AddReview -> if (currentNovel != null) {
                        AddReviewScreen(
                            novel = currentNovel!!,
                            novelDatabase = novelDatabase,
                            onReviewAdded = { currentScreen = Screen.NovelDetails },
                            onBackToDetails = { currentScreen = Screen.NovelDetails }

                        )
                    }
                }
            }
        }
    }
}

enum class Screen {
    ViewNovels,
    AddNovel,
    Favorites,
    NovelDetails,
    AddReview
}



@Composable
fun AddReviewScreen(novel: Novel, novelDatabase: NovelDatabase, onReviewAdded: () -> Unit , onBackToDetails: () -> Unit) {
    var reviewText by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    var showDialog by remember { mutableStateOf(false) }






    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {


        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Tu nombre") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )

        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = reviewText,
            onValueChange = { reviewText = it },
            label = { Text("Escribe tu reseña") },
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (reviewText.isBlank() || usuario.isBlank()) { // Validar ambos campos
                    showDialog = true
                } else {
                    novelDatabase.addReview(novel, reviewText, usuario)
                    onReviewAdded()
                }
            }
        )  {
            Text("Guardar reseña")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Error") },
                text = { Text("Ningún campo puede estar vacío") },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovelDetailsScreen(novel: Novel, novelDatabase: NovelDatabase, onBack: () -> Unit , onAddReviewClick: () -> Unit) {
    var isFavorite by remember { mutableStateOf(novel.getIsFavorite()) }
    val reviews by remember { mutableStateOf(novelDatabase.getReviewsForNovel(novel)) }

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
            Text("Sinopsis: ${novel.synopsis}", style = MaterialTheme.typography.bodyLarge)
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
            Button(onClick = onAddReviewClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Añadir reseña")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Reseñas:", style = MaterialTheme.typography.headlineSmall)
            reviews.forEach { review ->
                Text("${review.usuario}: ${review.reviewText}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun FavoritesScreen(novelDatabase: NovelDatabase, onBackToHome: () -> Unit, onNovelClick: (Novel) -> Unit) {
    var favoriteNovels by remember { mutableStateOf(novelDatabase.getFavoriteNovels()) }
    var refresh by remember { mutableStateOf(false) }

    Column{



        if (favoriteNovels.isEmpty()) {
            Text("No hay novelas favoritas.")
        } else {
            favoriteNovels.forEach { novel ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Título: ${novel.title}")
                        Text("Autor: ${novel.author}")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    IconButton(onClick = {
                        novelDatabase.toggleFavorite(novel)
                        favoriteNovels = novelDatabase.getFavoriteNovels()
                        refresh = !refresh
                    }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar de favoritos")
                    }

                    IconButton(onClick = { onNovelClick(novel) }) {
                        Icon(Icons.Filled.ArrowForward, contentDescription = "Ver detalles")
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
fun ViewNovelsScreen(novelDatabase: NovelDatabase, onAddNovelClick: () -> Unit, onNovelClick: (Novel) -> Unit, ) {
    val novels by remember { mutableStateOf(novelDatabase.getAllNovels()) }
    var currentNovel: Novel? by remember { mutableStateOf(null) }
    var currentScreen by remember { mutableStateOf(Screen.ViewNovels) }
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
                onBack = { currentNovel = null },
                onAddReviewClick = { currentScreen = Screen.AddReview }
            )

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
    var showDialog by remember { mutableStateOf(false) }
    var synopsis by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

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
            label = { Text("Título") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )
        OutlinedTextField(
            value = author,
            onValueChange = { author = it },
            label = { Text("Autor") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )
        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Año") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )
        OutlinedTextField(
            value = synopsis,
            onValueChange = { synopsis = it },
            label = { Text("Sinopsis") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (title.isBlank() || author.isBlank() || year.isBlank() || synopsis.isBlank()) {
                    showDialog = true
                } else {
                    val novel = Novel(title, author, year.toIntOrNull() ?: 0, synopsis)
                    novelDatabase.addNovel(novel)
                    onNovelAdded()
                }
            }
        ) {
            Text("Guardar Novela")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Error") },
                text = { Text("Ningún campo puede estar vacío") },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

