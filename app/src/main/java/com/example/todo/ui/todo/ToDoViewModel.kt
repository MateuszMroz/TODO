package com.example.todo.ui.todo

import androidx.lifecycle.ViewModel
import com.example.todo.data.repository.IToDoRepository
import javax.inject.Inject

class ToDoViewModel @Inject constructor(repository: IToDoRepository) : ViewModel() {

}