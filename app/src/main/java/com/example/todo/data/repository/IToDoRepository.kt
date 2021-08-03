package com.example.todo.data.repository

interface IToDoRepository {
    suspend fun fetchListToDo()
    suspend fun addToDo()
    suspend fun updateToDo()
    suspend fun removeToDo()
}