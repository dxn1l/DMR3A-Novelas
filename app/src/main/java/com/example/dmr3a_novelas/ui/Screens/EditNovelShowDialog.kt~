package com.example.dmr3a_novelas.ui.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
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
    var showDialogEmptylane by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    fun setShowDialogEmptylane(value: Boolean) {
        showDialogEmptylane = value
    }

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
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    )
                )
                OutlinedTextField(
                    value = author,
                    onValueChange = {
                        author = it
                    },
                    label = {
                        Text("Autor")
                    },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    )
                )
                OutlinedTextField(
                    value = year,
                    onValueChange = {
                        year = it
                    },
                    label = {
                        Text("Año")
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    )
                )
                OutlinedTextField(
                    value = synopsis,
                    onValueChange = {
                        synopsis = it
                    },
                    label = {
                        Text("Sinopsis")
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.contains("\n")
                        || author.contains("\n")
                        || year.contains("\n")
                    ) {
                        setShowDialogEmptylane(true)
                    } else {

                        val updatedNovel = Novel(
                            id = novel.id,
                            title = title,
                            author = author,
                            year = year.toInt(),
                            synopsis = synopsis,
                            _isFavorite = novel.getIsFavorite()
                        )
                        onEditNovel(updatedNovel)
                        onDismissRequest()
                    }
                }
            ) {
                Text("Guardar cambios")
            }
            if (showDialogEmptylane) {
                AlertDialog(
                    onDismissRequest = { setShowDialogEmptylane(false) },
                    title = { Text("Error") },
                    text = { Text("Todos los campos excepto synopsis no pueden tener líneas vacias") },
                    confirmButton = {
                        TextButton(onClick = { setShowDialogEmptylane(false) }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    )
}