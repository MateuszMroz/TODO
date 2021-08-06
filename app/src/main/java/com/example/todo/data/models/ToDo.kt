package com.example.todo.data.models

import java.util.*

data class ToDo(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = "",
    val pictureUrl: String? = null,
    val timestamp: Long = 0,
)