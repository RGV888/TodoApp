package com.example.todoapp.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.todoapp.viewmodels.SharedViewModel
import com.example.todoapp.viewmodels.TodoViewModel
import com.example.todoapp.viewmodels.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(sharedViewModel: SharedViewModel,navController: NavController) {
    val todoViewModel: TodoViewModel = hiltViewModel()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("To-Do List") }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                val todos by todoViewModel.todos.collectAsState()
                val uiState by todoViewModel.uiState.collectAsState()
                val errorMessage by sharedViewModel.errorMessage.collectAsState()
                val query = remember { mutableStateOf("") }
                var showDialog by remember { mutableStateOf(false) }

                Column(modifier = Modifier.fillMaxSize()) {

                    if (errorMessage != null && !showDialog) {
                        showDialog = true
                    }
                    if (showDialog) {
                        ErrorDialog(
                            message = errorMessage ?: "",
                            onDismiss = {
                                showDialog = false
                                sharedViewModel.clearErrorMessage()
                            }
                        )
                    }

                    OutlinedTextField(
                        value = query.value,
                        onValueChange = {
                            query.value = it
                            todoViewModel.searchTodos(it)
                        },
                        label = { Text("Search Todos") },
                        modifier = Modifier.fillMaxWidth().padding(16.dp).background(Color.White)
                    )


                    when(uiState){
                       is  UiState.Loading->{
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                        is UiState.Success->{
                            if (todos.isEmpty()) {
                                Text(
                                    text = "Press the + button to add a TODO item",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
                                )
                            }else{
                                LazyColumn {
                                    items(todos) {
                                        Card(modifier = Modifier.fillMaxWidth().padding(7.dp),
                                            shape = RoundedCornerShape(16.dp),
                                            elevation = CardDefaults.cardElevation(10.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor =  androidx.compose.ui.graphics.Color.White
                                            )
                                        ){
                                            Text(it.task,
                                                modifier = Modifier.padding(16.dp),
                                                color = androidx.compose.ui.graphics.Color.Black,
                                                fontSize = 14.sp)
                                        }

                                    }
                                }
                            }
                        }

                        is UiState.Error ->{
                            Toast.makeText(LocalContext.current, "Failed to load TODOs", Toast.LENGTH_SHORT).show()
                        }
                    }

                }


                FloatingActionButton(
                    onClick = { navController.navigate("add") },
                    modifier = Modifier
                        .padding(16.dp)  // Ensure padding for visibility
                        .align(Alignment.BottomEnd)  // Align to bottom-right corner
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Todo")
                }
            }
        }
    )
}

@Composable
fun ErrorDialog(message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Error") },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}







