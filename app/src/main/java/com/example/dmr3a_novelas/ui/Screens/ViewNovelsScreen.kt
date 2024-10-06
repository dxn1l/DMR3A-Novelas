package com.example.dmr3a_novelas.ui.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.dmr3a_novelas.DataBase.FirebaseNovelRepository
import com.example.dmr3a_novelas.DataBase.Novel
import com.example.dmr3a_novelas.ui.AppNavegation.Screen

@Composable
fun ViewNovelsScreen(novelRepository: FirebaseNovelRepository, onAddNovelClick: () -> Unit, onNovelClick: (Novel) -> Unit) {
    var novels by remember { mutableStateOf<List<Novel>>(emptyList()) }
    var currentNovel: Novel? by remember { mutableStateOf(null) }
    var currentScreen by remember { mutableStateOf(Screen.ViewNovels) }
    var refresh by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        novelRepository.getAllNovels { novels = it }
    }

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
                        novelRepository.toggleFavorite(novel)
                        refresh = !refresh
                    }) {
                        Icon(
                            imageVector = if (novel.getIsFavorite()) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (novel.getIsFavorite()) "Quitar de favoritos" else "Agregar a favoritos",
                            tint = if (novel.getIsFavorite()) Color.Red else Color.Gray
                        )
                    }
                    IconButton(onClick = {
                        novelRepository.removeNovel(novel)
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
                novelRepository = novelRepository,
                onBack = { currentNovel = null },
                onAddReviewClick = { currentScreen = Screen.AddReview }
            )

        }

    }
    LaunchedEffect(refresh) {

    }


}