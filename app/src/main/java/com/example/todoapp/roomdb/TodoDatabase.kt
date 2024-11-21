package com.example.todoapp.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoapp.roomdb.dao.TodoDao
import com.example.todoapp.roomdb.entitys.TodoEntity

@Database(entities = [TodoEntity::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}
