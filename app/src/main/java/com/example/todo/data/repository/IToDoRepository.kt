package com.example.todo.data.repository

import androidx.paging.PagingData
import com.example.todo.data.models.ToDo
import kotlinx.coroutines.flow.Flow

interface IToDoRepository {
    fun fetchListToDo(): Flow<PagingData<ToDo>>
    suspend fun addToDo(todo: ToDo): Result<Unit>
    suspend fun updateToDo(todo: ToDo): Result<Unit>
    suspend fun removeToDo(id: String): Result<Unit>
}