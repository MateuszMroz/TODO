package com.example.todo.data.repository

import androidx.paging.PagingData
import com.example.todo.data.DataSource
import com.example.todo.data.models.ToDo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ToDoRepository @Inject constructor(private val dataSource: DataSource) : IToDoRepository {

    override fun fetchListToDo(): Flow<PagingData<ToDo>> {
        return dataSource.fetchToDos()
    }

    override suspend fun addToDo(todo: ToDo): Result<Unit> {
        return dataSource.addToDO(todo)
    }

    override suspend fun updateToDo(todo: ToDo): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun removeToDo(id: String): Result<Unit> {
        TODO("Not yet implemented")
    }

}