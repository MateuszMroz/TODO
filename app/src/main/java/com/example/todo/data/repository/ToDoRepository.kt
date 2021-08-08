package com.example.todo.data.repository

import androidx.paging.PagingData
import com.example.todo.data.DataSource
import com.example.todo.data.models.ToDo
import com.example.todo.data.paging.FirestorePagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class ToDoRepository @Inject constructor(
    private val dataSource: DataSource,
) : IToDoRepository {
    override val pagingSource: FirestorePagingSource?
        get() = dataSource.pagingSource

    override fun fetchListToDo(): Flow<PagingData<ToDo>> {
        return dataSource.fetchToDos()
    }

    @ExperimentalCoroutinesApi
    override suspend fun realtimeUpdates(): Flow<Unit> {
        return dataSource.realtimeUpdates()
    }

    override suspend fun addToDo(todo: ToDo): Result<Unit> {
        return runCatching {
            dataSource.addToDO(todo)
        }.onSuccess {
            Result.success(it)
        }.onFailure {
            Timber.e(it)
            Result.failure<Unit>(it)
        }
    }

    override suspend fun fetchToDoById(id: String): Result<ToDo> {
        return runCatching { dataSource.fetchToDoById(id) }
            .onSuccess {
                Result.success(it)
            }.onFailure {
                Timber.e(it)
                Result.failure<ToDo>(it)
            }
    }

    override suspend fun updateToDo(todo: ToDo): Result<Unit> {
        return runCatching { dataSource.updateToDo(todo) }
            .onSuccess {
                Result.success(it)
            }.onFailure {
                Timber.e(it)
                Result.failure<Unit>(it)
            }
    }

    override suspend fun removeToDo(id: String): Result<Unit> {
        return runCatching { dataSource.deleteToDo(id) }
            .onSuccess {
                Result.success(it)
            }.onFailure {
                Timber.e(it)
                Result.failure<Unit>(it)
            }
    }
}