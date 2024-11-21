package com.example.todoapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddUiState?>(null)
    val uiState: StateFlow<AddUiState?> = _uiState


     fun addTodo(task: String) {

         _uiState.value = AddUiState.Loading
         viewModelScope.launch {
             delay(3000)

             if (task.equals("Error", ignoreCase = true)) {
                 _uiState.value = AddUiState.Error("Failed to add Todo due to error.")
             } else {
                 todoRepository.addTodo(task)
                 _uiState.value = AddUiState.Success("")
             }
         }

    }
}

sealed class AddUiState {
    object Loading : AddUiState()
    data class Success(val data: String) : AddUiState()
    data class Error(val message: String) : AddUiState()
}

