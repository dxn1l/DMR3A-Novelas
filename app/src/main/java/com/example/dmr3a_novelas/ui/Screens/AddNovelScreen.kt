package com.example.dmr3a_novelas.ui.Screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.dmr3a_novelas.DataBase.FirebaseNovelRepository
import com.example.dmr3a_novelas.DataBase.Novel

@Composable
fun AddNovelScreen(novelRepository: FirebaseNovelRepository, onNovelAdded: () -> Unit) {

    var id by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var synopsis by remember { mutableStateOf("") }
    var showDialogEmptylane by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = id,
            onValueChange = { id = it },
            label = { Text("ID") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )
        OutlinedTextField(
            value = author,
            onValueChange = { author = it },
            label = { Text("Autor") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )
        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Año") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )
        OutlinedTextField(
            value = synopsis,
            onValueChange = { synopsis = it },
            label = { Text("Sinopsis") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (id.isBlank()
                    || title.isBlank()
                    || author.isBlank()
                    || year.isBlank()
                    || synopsis.isBlank()) {
                    showDialog = true
                }else if(id.contains("\n" )
                    || title.contains("\n" )
                    || author.contains("\n" )
                    || year.contains("\n" )
                ){
                    showDialogEmptylane = true
                } else {
                    val novel = Novel(id= id, title, author, year.toIntOrNull() ?: 0, synopsis)
                    novelRepository.addNovel(novel)
                    onNovelAdded()
                }
            }
        ) {
            Text("Guardar Novela")
        }

        if (showDialogEmptylane){
            AlertDialog(
                onDismissRequest = { showDialogEmptylane = false },
                title = { Text("Error") },
                text = { Text("Todos los campos excepto synopsis no pueden tener líneas vacias" ) },
                confirmButton = {
                    TextButton(onClick = { showDialogEmptylane = false }) {
                        Text("OK")
                    }
                }
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Error") },
                text = { Text("Ningún campo puede estar vacío") },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

