package com.example.todo.data.repository

import androidx.paging.PagingData
import com.example.todo.data.models.ToDo
import com.example.todo.data.paging.FirestorePagingSource
import kotlinx.coroutines.flow.Flow

interface IToDoRepository {
    val pagingSource: FirestorePagingSource?

    fun fetchListToDo(): Flow<PagingData<ToDo>>
    suspend fun addToDo(title: String, desc: String, pictureUrl: String?): Result<Unit>
    suspend fun fetchToDoById(id: String): Result<ToDo>
    suspend fun updateToDo(todo: ToDo): Result<Unit>
    suspend fun removeToDo(id: String): Result<Unit>
}