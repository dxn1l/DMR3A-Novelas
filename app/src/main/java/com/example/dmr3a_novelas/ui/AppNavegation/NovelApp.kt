package com.example.dmr3a_novelas.ui.AppNavegation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.dmr3a_novelas.DataBase.FirebaseNovelRepository
import com.example.dmr3a_novelas.DataBase.Novel
import com.example.dmr3a_novelas.ui.Screens.AddNovelScreen
import com.example.dmr3a_novelas.ui.Screens.AddReviewScreen
import com.example.dmr3a_novelas.ui.Screens.FavoritesScreen
import com.example.dmr3a_novelas.ui.Screens.NovelDetailsScreen
import com.example.dmr3a_novelas.ui.Screens.ViewNovelsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovelApp() {

    val novelRepository = remember { FirebaseNovelRepository()}
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
                    Screen.ViewNovels -> ViewNovelsScreen(novelRepository, onAddNovelClick = { currentScreen = Screen.AddNovel }) { novel ->
                        currentNovel = novel
                        currentScreen = Screen.NovelDetails
                    }
                    Screen.AddNovel -> AddNovelScreen(novelRepository) { currentScreen = Screen.ViewNovels }
                    Screen.Favorites -> FavoritesScreen(novelRepository, onNovelClick = { novel ->
                        currentNovel = novel
                        currentScreen = Screen.NovelDetails
                    })
                    Screen.NovelDetails -> if (currentNovel != null) {
                        NovelDetailsScreen(
                            novel = currentNovel!!,
                            novelRepository = novelRepository,
                            onAddReviewClick = { currentScreen = Screen.AddReview } ,
                            onEditNovel = { updatedNovel ->
                                novelRepository.updateNovel(updatedNovel)
                            },
                            onDeleteReviewClick = { review ->
                                novelRepository.deleteReview(review,
                                    onSuccess = { /* Manejo de éxito (por ejemplo, mostrar un mensaje) */ },
                                    onError = { error -> /* Manejo de error (por ejemplo, mostrar un mensaje de error) */ }
                                )
                            }

                        )
                    }
                    Screen.AddReview -> if (currentNovel != null) {
                        AddReviewScreen(
                            novel = currentNovel!!,
                            novelRepository = novelRepository,
                            onReviewAdded = { currentScreen = Screen.NovelDetails },
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