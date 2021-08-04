package com.example.todo.ui.list_todo.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.todo.data.models.ToDo

class ToDoDiffCallback : DiffUtil.ItemCallback<ToDo>() {
    override fun areItemsTheSame(oldItem: ToDo, newItem: ToDo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ToDo, newItem: ToDo): Boolean {
        return oldItem == newItem
    }
}