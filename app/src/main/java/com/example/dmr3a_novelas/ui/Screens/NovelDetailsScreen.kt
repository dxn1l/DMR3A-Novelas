package com.example.dmr3a_novelas.ui.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.dmr3a_novelas.DataBase.Novel
import com.example.dmr3a_novelas.DataBase.NovelDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovelDetailsScreen(novel: Novel, novelDatabase: NovelDatabase, onBack: () -> Unit, onAddReviewClick: () -> Unit) {
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