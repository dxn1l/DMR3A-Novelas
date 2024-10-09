package com.example.dmr3a_novelas.ui.Reviews

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dmr3a_novelas.DataBase.Review

@Composable
fun ReviewItem(
    review: Review,
    onDeleteClick: (Review) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Usuario: ${review.usuario}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Reseña: ${review.reviewText}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Row {
                IconButton(onClick = { onDeleteClick(review) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar reseña")
                }
            }
        }
    }


}
