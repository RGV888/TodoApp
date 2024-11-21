package com.example.todoapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.repository.TodoRepository
import com.example.todoapp.roomdb.entitys.TodoEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _todos = MutableStateFlow<List<TodoEntity>>(emptyList())
    val todos: StateFlow<List<TodoEntity>> = _todos

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val uiState: StateFlow<UiState<Unit>> = _uiState

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        getTodos()
    }

     fun getTodos() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                todoRepository.getAllTodos().collect { todoList ->
                    _todos.value = todoList
                    _uiState.value = UiState.Success(Unit)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to get Todos")
                _error.value = e.message
            }
        }
    }

    fun searchTodos(query: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                todoRepository.searchTodos(query).collect { todoList ->
                    _todos.value = todoList
                    _uiState.value = UiState.Success(Unit)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to get Todos")
            }
        }
    }
}

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

