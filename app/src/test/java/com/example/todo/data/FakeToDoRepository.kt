package com.example.todo.data

import androidx.paging.PagingData
import com.example.todo.data.models.ToDo
import com.example.todo.data.paging.FirestorePagingSource
import com.example.todo.data.repository.IToDoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class FakeToDoRepository : IToDoRepository {
    var todoData: LinkedHashMap<String, ToDo> = LinkedHashMap()

    var shouldThrowError: Boolean = false
    var shouldThrowErrorFetchToDo: Boolean = false

    val exceptionMsg = "Test Error"

    override val pagingSource: FirestorePagingSource?
        get() = null

    override fun fetchListToDo(): Flow<PagingData<ToDo>> {
        return if (shouldThrowError) {
            flow {
                Throwable(exceptionMsg)
            }
        } else {
            flow {
                PagingData.from(todoData.map { it.value })
            }
        }
    }

    override suspend fun realtimeUpdates(): Flow<Unit> {
        return if (shouldThrowError) {
            flow {
                Throwable(exceptionMsg)
            }
        } else {
            flow {
                emit(Unit)
            }
        }
    }

    override suspend fun addToDo(todo: ToDo): Result<Unit> {
        todoData[todo.id] = todo
        if (shouldThrowError) {
            return Result.failure(Throwable(exceptionMsg))
        }
        return Result.success(Unit)
    }

    override suspend fun fetchToDoById(id: String): Result<ToDo> {
        return if (shouldThrowErrorFetchToDo) {
            Result.failure<ToDo>(Throwable(exceptionMsg))
        } else {
            val todo = todoData[id]
            return if (todo != null) {
                Result.success(todo)
            } else {
                Result.failure<ToDo>(Throwable("No found todo"))
            }
        }
    }

    override suspend fun updateToDo(todo: ToDo): Result<Unit> {
        return if (shouldThrowError) {
            Result.failure<Unit>(Throwable(exceptionMsg))
        } else {
            todoData[todo.id]?.apply {
                title = todo.title
                description = todo.description
                pictureUrl = todo.pictureUrl
            }
            Result.success(Unit)
        }
    }

    override suspend fun removeToDo(id: String): Result<Unit> {
        return if (shouldThrowError) {
            Result.failure<Unit>(Throwable(exceptionMsg))
        } else {
            todoData.remove(id)
            Result.success(Unit)
        }
    }

    fun clearData() {
        todoData.clear()
    }
}