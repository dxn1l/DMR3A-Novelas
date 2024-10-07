package com.example.dmr3a_novelas.ui.Reviews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dmr3a_novelas.DataBase.Review

@Composable
fun ReviewsList(
    reviews: List<Review>,
    onDeleteClick: (Review) -> Unit
) {


    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        reviews.forEach { review ->
            ReviewItem(
                review = review,
                onDeleteClick = onDeleteClick
            )
        }
    }
}