package com.example.dmr3a_novelas.ui.Screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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


    LaunchedEffect(Unit) {
        novelRepository.getFavoriteNovels(
            onResult = { favoriteNovels = it },
            onError = { error ->
                Log.e("Error", "Error al obtener las novelas favoritas: ${error.message}")
            }
        )
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
    ){
        if (favoriteNovels.isEmpty()) {
            Text("No hay novelas favoritas.")
        } else {
            favoriteNovels.forEach { novel ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                    var showDialog by remember { mutableStateOf(false) }
                    var showDialogDelete by remember { mutableStateOf(false) }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Título: ${novel.title}")
                            Text("Autor: ${novel.author}")
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        IconButton(onClick = {
                            showDialog = true

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
                            showDialogDelete = true
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Eliminar de favoritos")
                        }
                        if (showDialogDelete) {
                            AlertDialog(
                                onDismissRequest = { showDialogDelete = false },
                                title = { Text("Eliminar de favoritos") },
                                text = { Text("¿Estás seguro de que deseas eliminar esta novela de tus favoritos?") },
                                confirmButton = {
                                    Button(onClick = {
                                        novelRepository.toggleFavorite(novel)
                                        showDialogDelete = false
                                    }) {
                                        Text("Eliminar")
                                    }
                                },
                                dismissButton = {
                                    Button(onClick = { showDialogDelete = false }) {
                                        Text("Cancelar")
                                    }
                                }
                            )
                        }

                        IconButton(onClick = { onNovelClick(novel) }) {
                            Icon(Icons.Filled.ArrowForward, contentDescription = "Ver detalles")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

        }
    }


}