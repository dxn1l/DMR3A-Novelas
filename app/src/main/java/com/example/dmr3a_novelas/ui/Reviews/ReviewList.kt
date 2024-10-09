package com.example.dmr3a_novelas.ui.Reviews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dmr3a_novelas.DataBase.Review

@Composable
fun ReviewsList(
    reviews: List<Review>,
    onDeleteClick: (Review) -> Unit
) {
    var localReviews by remember { mutableStateOf(reviews) }
    var showDialog by remember { mutableStateOf(false) }
    var reviewToDelete by remember { mutableStateOf<Review?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        reviews.forEach { review ->
            ReviewItem(
                review = review,
                onDeleteClick = { review ->
                    reviewToDelete = review
                    showDialog = true
                }
            )
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Eliminar reseña") },
            text = { Text("¿Estás seguro de que deseas eliminar esta reseña?") },
            confirmButton = {
                Button(onClick = {
                    onDeleteClick(reviewToDelete!!)
                    localReviews = localReviews.filter { it.id != reviewToDelete!!.id }
                    showDialog = false
                }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}