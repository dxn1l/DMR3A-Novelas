package com.example.dmr3a_novelas.ui.Screens

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.dmr3a_novelas.DataBase.FirebaseNovelRepository
import com.example.dmr3a_novelas.DataBase.Novel
import com.example.dmr3a_novelas.ui.BroadCast.InternetConnectivityReceiver
import com.example.dmr3a_novelas.ui.Notification.sendNotification


@Composable
fun ViewNovelsScreen(
    novelRepository: FirebaseNovelRepository,
    onAddNovelClick: () -> Unit,
    onNovelClick: (Novel) -> Unit,
    novelAdded: Boolean
) {
    var novels by remember { mutableStateOf<List<Novel>>(emptyList()) }
    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current


    LaunchedEffect(novelAdded) {
        if (novelAdded) {
            snackbarHostState.showSnackbar("Novela agregada exitosamente",
                duration = SnackbarDuration.Short,
                actionLabel = "Aceptar"
            )
        }
    }


    LaunchedEffect(Unit) {
        novelRepository.getAllNovels(
            onResult = { novelsList ->
                novels = novelsList
            },
            onError = { error ->
                Log.e("Error", "Error al obtener las novelas: ${error.message}")
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }

    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
        ) {

            IconButton(onClick = onAddNovelClick, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir Novela")
            }


            novels.mapIndexed { index, novel ->
                key(novel.title) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        var showDialog by remember { mutableStateOf(false) }
                        var showDialogDelete by remember { mutableStateOf(false) }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Título: ${novel.title}")
                                Text("Autor: ${novel.author}")
                            }

                            IconButton(onClick = { showDialog = true }) {
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
                            }) {
                                Icon(
                                    imageVector = if (novel.getIsFavorite()) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = if (novel.getIsFavorite()) "Quitar de favoritos" else "Agregar a favoritos",
                                    tint = if (novel.getIsFavorite()) Color.Red else Color.Gray
                                )
                            }


                            IconButton(onClick = { showDialogDelete = true }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                            }


                            if (showDialogDelete) {
                                AlertDialog(
                                    onDismissRequest = { showDialogDelete = false },
                                    title = { Text("Eliminar novela") },
                                    text = { Text("¿Estás seguro de que deseas eliminar esta novela?") },
                                    confirmButton = {
                                        Button(onClick = {
                                            novelRepository.removeNovel(novel)
                                            showDialogDelete = false
                                            sendNotification(
                                                context = context,
                                                title = "Novela Guardada",
                                                message = "La novela ${novel.title} ha sido guardada",
                                                notificationId = 2
                                            )
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
}
