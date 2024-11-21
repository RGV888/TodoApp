package com.example.todoapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.todoapp.viewmodels.AddTodoViewModel
import com.example.todoapp.viewmodels.AddUiState
import com.example.todoapp.viewmodels.SharedViewModel

@Composable
fun AddTodoScreen(sharedViewModel: SharedViewModel,navController: NavController) {
    val viewModel:AddTodoViewModel= hiltViewModel()
    Column(modifier = Modifier.padding(16.dp)) {
        var task by remember { mutableStateOf("") }
        val uiState by viewModel.uiState.collectAsState()

        OutlinedTextField(
            value = task,
            singleLine = true,
            onValueChange = { task = it },
            label = { Text("Enter Task") },
            modifier = Modifier.fillMaxWidth().background(Color.White)
        )
        Button(
            onClick = {
                viewModel.addTodo(task)
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Add TODO")
        }

        when (uiState) {
            is AddUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is AddUiState.Success -> {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }

            }
            is AddUiState.Error -> {
                val errorMessage = (uiState as AddUiState.Error).message
                sharedViewModel.setErrorMessage(errorMessage)
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }
            null->{

            }
        }

    }



}