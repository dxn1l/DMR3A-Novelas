package com.example.dmr3a_novelas.ui.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dmr3a_novelas.DataBase.FirebaseNovelRepository
import com.example.dmr3a_novelas.DataBase.Novel


@Composable
fun FavoritesScreen(novelRepository: FirebaseNovelRepository, onBackToHome: () -> Unit, onNovelClick: (Novel) -> Unit) {
    var favoriteNovels by remember { mutableStateOf<List<Novel>>(emptyList()) }
    var refresh by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        novelRepository.getFavoriteNovels { favoriteNovels = it }
    }

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
                        novelRepository.toggleFavorite(novel)
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