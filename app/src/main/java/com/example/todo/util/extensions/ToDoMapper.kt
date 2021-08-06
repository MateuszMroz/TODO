package com.example.todo.util.extensions

import com.example.todo.data.models.ToDo
import java.util.*
import javax.inject.Inject

class ToDoMapper @Inject constructor() {
    private val id = "id"
    private val title = "title"
    private val description = "desc"
    private val pictureUrl = "picture_url"
    private val timestamp = "time"

    fun convertToHashMap(todo: ToDo): HashMap<String, Any?> {
        return hashMapOf(
            id to todo.id,
            title to todo.title,
            description to todo.description,
            pictureUrl to todo.pictureUrl,
            timestamp to todo.timestamp,
        )
    }

    fun convertToToDo(title: String, desc: String, pictureUrl: String?, time: Long): ToDo {
        return ToDo(
            title = title,
            description = desc,
            pictureUrl = pictureUrl,
            timestamp = time,
        )
    }
}