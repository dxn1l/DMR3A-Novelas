package com.example.dmr3a_novelas.ui.Screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
fun FavoritesScreen(novelRepository: FirebaseNovelRepository,onNovelClick: (Novel) -> Unit) {
    var favoriteNovels by remember { mutableStateOf<List<Novel>>(emptyList()) }
    var refresh by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        novelRepository.getFavoriteNovels(
            onResult = { favoriteNovels = it },
            onError = { error ->
                Log.e("Error", "Error al obtener las novelas favoritas: ${error.message}")
            }
        )
    }

    Column{



        if (favoriteNovels.isEmpty()) {
            Text("No hay novelas favoritas.")
        } else {
            favoriteNovels.forEach { novel ->
                var showDialog by remember { mutableStateOf(false) }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Título: ${novel.title}")
                        Text("Autor: ${novel.author}")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    IconButton(onClick = {
                        showDialog=true

                    }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar")
                    }

                    if (showDialog) {
                        EditNovelDialog(
                            novel = novel,
                            onDismissRequest = { showDialog = false },
                            onEditNovel = { updatedNovel ->
                                novelRepository.updateNovel(updatedNovel)
                            },
                        )
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