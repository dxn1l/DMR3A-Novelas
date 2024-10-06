package com.example.dmr3a_novelas.ui.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.dmr3a_novelas.DataBase.Novel

@Composable
fun EditNovelDialog(
    novel: Novel,
    onDismissRequest: () -> Unit,
    onEditNovel: (Novel) -> Unit
) {

    var title by remember { mutableStateOf(novel.title) }
    var author by remember { mutableStateOf(novel.author) }
    var year by remember { mutableStateOf(novel.year.toString()) }
    var synopsis by remember { mutableStateOf(novel.synopsis) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Editar novela") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    label = {
                        Text("Título")
                    }
                )
                OutlinedTextField(
                    value = author,
                    onValueChange = {
                        author = it
                    },
                    label = {
                        Text("Autor")
                    }
                )
                OutlinedTextField(
                    value = year,
                    onValueChange = {
                        year = it
                    },
                    label = {
                        Text("Año")
                    }
                )
                OutlinedTextField(
                    value = synopsis,
                    onValueChange = {
                        synopsis = it
                    },
                    label = {
                        Text("Sinopsis")
                    }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val updatedNovel = Novel(
                        id = novel.id,
                        title = title,
                        author = author,
                        year = year.toInt(),
                        synopsis = synopsis
                    )
                    onEditNovel(updatedNovel)
                }
            ) {
                Text("Guardar cambios")
            }
        }
    )
}