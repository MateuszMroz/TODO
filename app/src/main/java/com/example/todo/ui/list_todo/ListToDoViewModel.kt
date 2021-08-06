package com.example.todo.ui.list_todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todo.data.repository.IToDoRepository
import com.example.todo.util.extensions.Event
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ListToDoViewModel @Inject constructor(private val repository: IToDoRepository) : ViewModel() {

    private val _newToDo = MutableLiveData<Event<Unit>>()
    val newToDo: LiveData<Event<Unit>> = _newToDo


    fun addNewToDo() {
        _newToDo.value = Event(Unit)
    }
}