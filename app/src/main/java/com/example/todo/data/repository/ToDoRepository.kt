package com.example.todo.data.repository

import com.example.todo.data.DataSource
import javax.inject.Inject

class ToDoRepository @Inject constructor(private val dataSource: DataSource): IToDoRepository {

    override suspend fun fetchListToDo() {
        TODO("Not yet implemented")
    }

    override suspend fun addToDo() {
        TODO("Not yet implemented")
    }

    override suspend fun updateToDo() {
        TODO("Not yet implemented")
    }

    override suspend fun removeToDo() {
        TODO("Not yet implemented")
    }

}