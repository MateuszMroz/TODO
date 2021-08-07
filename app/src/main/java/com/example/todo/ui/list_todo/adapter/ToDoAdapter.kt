package com.example.todo.ui.list_todo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.data.models.ToDo
import com.example.todo.databinding.ItemTodoBinding
import com.example.todo.ui.list_todo.ListToDoViewModel

class ToDoAdapter(private val viewModel: ListToDoViewModel) :
    PagingDataAdapter<ToDo, ToDoAdapter.ToDoViewHolder>(ToDoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val todo = getItem(position) ?: return
        holder.bind(todo)
    }

    inner class ToDoViewHolder(private val binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: ToDo) {
            binding.item = todo
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }
}