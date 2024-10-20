package com.example.dmr3a_novelas.ui.Screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.dmr3a_novelas.DataBase.FirebaseNovelRepository
import com.example.dmr3a_novelas.DataBase.Novel
import com.example.dmr3a_novelas.DataBase.Review
import com.example.dmr3a_novelas.ui.Reviews.ReviewsList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovelDetailsScreen(novel: Novel,
                       novelRepository: FirebaseNovelRepository,
                       onAddReviewClick: () -> Unit,
                       onEditNovel: (Novel) -> Unit,
                       onDeleteReviewClick: (Review) -> Unit,
                       reviewAdded: Boolean) {
    var isFavorite by remember { mutableStateOf(novel.getIsFavorite()) }
    var reviews by remember { mutableStateOf<List<Review>>(emptyList()) }
    var currentNovel by remember { mutableStateOf(novel) }
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(reviewAdded) {
        if (reviewAdded){
            snackbarHostState.showSnackbar("Reseña agregada exitosamente",
                duration = SnackbarDuration.Short,
                actionLabel = "Aceptar"
            )
        }
    }

    LaunchedEffect(currentNovel) {
        novelRepository.getReviewsForNovel(
            currentNovel,
            onResult = { reviewsList ->
                reviews = reviewsList
            },
            onError = { error ->
                Log.e("Error", "Error al obtener las reseñas para la novela: ${error.message}")
            }
        )
    }

    LaunchedEffect(isFavorite) {
        currentNovel.id?.let {
            novelRepository.getNovelById(it,
                onResult = { novel ->
                    currentNovel = novel
                },
                onError = { error ->
                    Log.e("Error", "Error al obtener la novela: ${error.message}")
                }
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(currentNovel.title) },

                )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)) {
            Text("Autor: ${currentNovel.author}", style = MaterialTheme.typography.bodyLarge)
            Text("Año: ${currentNovel.year}", style = MaterialTheme.typography.bodyLarge)
            Text("Sinopsis: ${currentNovel.synopsis}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            var showDialog by remember { mutableStateOf(false) }
            Row(verticalAlignment = Alignment.CenterVertically) {

                IconButton(onClick = {
                    showDialog = true

                }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar")
                }

                if (showDialog) {
                    EditNovelDialog(
                        novel = currentNovel,
                        onDismissRequest = { showDialog = false },
                        onEditNovel =  {updatedNovel ->
                            onEditNovel(updatedNovel)
                            currentNovel = updatedNovel
                            isFavorite = updatedNovel.getIsFavorite() }
                    )
                }
                IconButton(onClick = {
                    novelRepository.toggleFavorite(currentNovel)
                    isFavorite = !isFavorite
                }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }
            }

            IconButton(onClick = onAddReviewClick, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir Reseña")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Reseñas:", style = MaterialTheme.typography.headlineSmall)

            if (reviews.isNotEmpty()) {


                ReviewsList(
                    reviews = reviews,
                    onDeleteClick = onDeleteReviewClick
                )

            } else {
                Text("Cargando reseñas...")
            }



        }
    }

}





