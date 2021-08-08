package com.example.todo.ui.list_todo

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.todo.R
import com.example.todo.data.repository.IToDoRepository
import com.example.todo.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListToDoViewModel @Inject constructor(private val repository: IToDoRepository) : ViewModel() {

    private val _newToDo = MutableLiveData<Event<Unit>>()
    val newToDo: LiveData<Event<Unit>> = _newToDo

    private val _editToDo = MutableLiveData<Event<String>>()
    val editToDo: LiveData<Event<String>> = _editToDo

    private val _removeToDo = MutableLiveData<Event<String>>()
    val removeToDo: LiveData<Event<String>> = _removeToDo

    private val _errorMsg = MutableLiveData<Event<String?>>()
    val errorMsg: LiveData<Event<String?>> = _errorMsg

    private val _successMsg = MutableLiveData<Event<Int>>()
    val successMsg: LiveData<Event<Int>> = _successMsg

    val isLoading = MutableLiveData(false)

    val todos = repository.fetchListToDo()
        .cachedIn(viewModelScope)

    init {
        realtimeUpdated()
    }

    fun addNewToDo() {
        _newToDo.value = Event(Unit)
    }

    fun editToDo(id: String) {
        _editToDo.value = Event(id)
    }

    fun removeToDo(id: String): Boolean {
        _removeToDo.value = Event(id)
        return true
    }

    suspend fun onRemoveTask(id: String) {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.removeToDo(id)
            if (result.isSuccess) {
                isLoading.value = false
                setSuccessMsg(R.string.note_removed_success)
                onRefresh()
            } else {
                isLoading.value = false
                setErrorMsg(result.exceptionOrNull()?.message)
            }
        }
    }

    fun onRefresh() {
        repository.pagingSource?.invalidate()
    }

    private fun realtimeUpdated() {
        viewModelScope.launch {
            repository.realtimeUpdates().collect {
                onRefresh()
            }
        }
    }

    private fun setErrorMsg(message: String?) {
        _errorMsg.value = Event(message)
    }

    private fun setSuccessMsg(@StringRes msgId: Int) {
        _successMsg.value = Event(msgId)
    }
}