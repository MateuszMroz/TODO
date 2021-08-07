package com.example.todo.data.models

import com.google.firebase.firestore.PropertyName
import java.util.*

data class ToDo(
    val id: String = UUID.randomUUID().toString(),
    var title: String = "",
    @get: PropertyName("desc")
    @set: PropertyName("desc")
    var description: String = "",
    @get: PropertyName("picture_url")
    @set: PropertyName("picture_url")
    var pictureUrl: String? = null,
    @get: PropertyName("time")
    @set: PropertyName("time")
    var timestamp: Long = 0,
)