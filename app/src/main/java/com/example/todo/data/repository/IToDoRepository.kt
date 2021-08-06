package com.example.todo.data.repository

import com.example.todo.data.models.ToDo

interface IToDoRepository {
    suspend fun fetchListToDo(): List<ToDo>
    suspend fun addToDo(todo: ToDo): Result<Unit>
    suspend fun updateToDo(todo: ToDo): Result<Unit>
    suspend fun removeToDo(id: String): Result<Unit>
}