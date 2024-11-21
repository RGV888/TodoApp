package com.example.todoapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.screens.AddTodoScreen
import com.example.todoapp.screens.TodoListScreen
import com.example.todoapp.viewmodels.SharedViewModel
import com.example.todoapp.viewmodels.TodoViewModel

@Composable
fun TodoAppNavigation() {
    val sharedViewModel: SharedViewModel = hiltViewModel()
    val navController = rememberNavController()
    NavHost(navController, startDestination = "main") {
        composable("main") {
            TodoListScreen(sharedViewModel=sharedViewModel,navController = navController)
        }
        composable("add") {
            AddTodoScreen(sharedViewModel=sharedViewModel,navController = navController)
        }
    }
}


