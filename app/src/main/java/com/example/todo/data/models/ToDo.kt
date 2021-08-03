package com.example.todo.data.models

data class ToDo(
    val id: String,
    val title: String,
    val description: String,
    val pictureUrl: String?,
    val timestamp: Long,
)