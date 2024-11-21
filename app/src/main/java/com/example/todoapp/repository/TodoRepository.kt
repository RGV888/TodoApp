package com.example.todoapp.repository

import com.example.todoapp.roomdb.dao.TodoDao
import com.example.todoapp.roomdb.entitys.TodoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoRepository @Inject constructor(private val todoDao: TodoDao) {

    fun getAllTodos(): Flow<List<TodoEntity>> {
        return todoDao.getAllTodos()
    }

    fun searchTodos(query: String): Flow<List<TodoEntity>> {
        return todoDao.searchTodos("%$query%")
    }

    suspend fun addTodo(task: String) {
        todoDao.insertTodo(TodoEntity(task = task))
    }
}

