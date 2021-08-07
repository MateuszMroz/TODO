package com.example.todo.ui.todo

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.repository.IToDoRepository
import com.example.todo.util.ToDoMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ToDoViewModel @Inject constructor(
    private val repository: IToDoRepository,
    private val mapper: ToDoMapper
) : ViewModel() {

    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val pictureUrl = MutableLiveData<String?>()

    val isLoading = MutableLiveData(false)
    val canSaveToDo = MediatorLiveData<Boolean>().apply {
        addSource(title) {
            !it.isNullOrEmpty()
        }
        addSource(description) {
            !it.isNullOrEmpty()
        }
    }


    fun saveToDo() {
        viewModelScope.launch {
            isLoading.value = true
            runCatching {
                repository.addToDo(
                    mapper.convertToToDo(
                        title.value!!,
                        description.value!!,
                        pictureUrl.value,
                        System.currentTimeMillis()
                    )
                )
            }.onSuccess {
                isLoading.value = false
                Timber.d("SUCCESS")
            }.onFailure {
                isLoading.value = false
                Timber.d("FAILURE")
            }
        }
    }
}