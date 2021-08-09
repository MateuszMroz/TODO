package com.example.todo.ui.todo

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.example.todo.R
import com.example.todo.data.models.ToDo
import com.example.todo.data.repository.IToDoRepository
import com.example.todo.util.Event
import com.example.todo.util.ToDoMapper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class ToDoViewModel @AssistedInject constructor(
    private val repository: IToDoRepository,
    private val mapper: ToDoMapper,
    @Assisted private val todoId: String?,
) : ViewModel() {

    private val _errorMsg = MutableLiveData<Event<String?>>()
    val errorMsg: LiveData<Event<String?>> = _errorMsg

    private val _successMsg = MutableLiveData<Event<Int>>()
    val successMsg: LiveData<Event<Int>> = _successMsg

    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val pictureUrl = MutableLiveData<String?>()

    val isLoading = MutableLiveData(false)

    private var todoEdit: ToDo? = null

    init {
        fetchToDo()
    }

    fun saveToDo() {
        if (todoId != null) {
            updateToDo()
        } else {
            saveNewToDo()
        }
    }

    fun fetchToDo() {
        if (todoId != null) {
            viewModelScope.launch {
                isLoading.value = true
                val result = repository.fetchToDoById(todoId)

                if (result.isSuccess) {
                    isLoading.value = false
                    todoEdit = result.getOrNull()
                    updatedToDoToEdit()
                } else {
                    isLoading.value = false
                    setErrorMsg(result.exceptionOrNull()?.message)
                }
            }
        }
    }

    private fun updateToDo() {
        viewModelScope.launch {
            todoEdit?.title = title.value!!
            todoEdit?.description = description.value!!
            todoEdit?.pictureUrl = pictureUrl.value

            todoEdit?.let {
                isLoading.value = true
                val result = repository.updateToDo(it)
                if (result.isSuccess) {
                    isLoading.value = false
                    setSuccessMsg(R.string.msg_todo_updated_success)
                } else {
                    isLoading.value = false
                    setErrorMsg(result.exceptionOrNull()?.message)
                }
            }
        }
    }

    private fun saveNewToDo() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.addToDo(
                mapper.convertToToDo(
                    title.value!!,
                    description.value!!,
                    pictureUrl.value,
                    System.currentTimeMillis()
                )
            )

            if (result.isSuccess) {
                isLoading.value = false
                setSuccessMsg(R.string.msg_todo_save_success)
            } else {
                isLoading.value = false
                setErrorMsg(result.exceptionOrNull()?.message)
            }
        }
    }

    private fun updatedToDoToEdit() {
        title.value = todoEdit?.title
        description.value = todoEdit?.description
        pictureUrl.value = todoEdit?.pictureUrl
    }

    private fun setErrorMsg(message: String?) {
        _errorMsg.value = Event(message)
    }

    private fun setSuccessMsg(@StringRes msgId: Int) {
        _successMsg.value = Event(msgId)
    }

    @AssistedFactory
    interface ToDoAssistedFactory {
        fun create(@Assisted todoId: String?): ToDoViewModel
    }

    class Factory(
        private val assistedFactory: ToDoAssistedFactory,
        private val todoId: String?,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return assistedFactory.create(todoId) as T
        }
    }
}
