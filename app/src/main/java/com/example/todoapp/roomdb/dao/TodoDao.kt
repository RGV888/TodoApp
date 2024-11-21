package com.example.todoapp.roomdb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.todoapp.roomdb.entitys.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
//    @Insert
//    suspend fun insertTodo(todo: TodoEntity)
//
//    @Query("SELECT * FROM todos WHERE task LIKE :query")
//    fun getTodos(query: String): Flow<List<TodoEntity>>
//
//    @Query("SELECT * FROM todos")
//    suspend fun getAllTodosList(): List<TodoEntity>

//    @Delete
//    suspend fun deleteTodo(todo: TodoEntity)

    @Insert
    suspend fun insertTodo(todo: TodoEntity)

    @Query("SELECT * FROM todo_table")
    fun getAllTodos(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todo_table WHERE task LIKE :query")
    fun searchTodos(query: String): Flow<List<TodoEntity>>
}
