package com.example.todo.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.todo.data.models.ToDo
import com.example.todo.data.paging.FirestorePagingSource
import com.example.todo.di.coroutine.SupervisorJobCoroutinesIO
import com.example.todo.util.COLLECTION_NAME
import com.example.todo.util.PAGE_SIZE
import com.example.todo.util.ToDoMapper
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resumeWithException

class DataSource @Inject constructor(
    private val db: FirebaseFirestore,
    private val mapper: ToDoMapper,
    @SupervisorJobCoroutinesIO private val coroutineContext: CoroutineContext
) {

    fun fetchToDos(): Flow<PagingData<ToDo>> {
        return Pager(
            PagingConfig(
                pageSize = PAGE_SIZE
            )
        ) {
            FirestorePagingSource(db)
        }.flow
    }

    @ExperimentalCoroutinesApi
    suspend fun addToDO(todo: ToDo): Result<Unit> = withContext(coroutineContext) {
        suspendCancellableCoroutine {
            db.collection(COLLECTION_NAME)
                .add(mapper.convertToHashMap(todo))
                .addOnSuccessListener { _ ->
                    it.resume(Result.success(Unit), null)
                }
                .addOnFailureListener { e ->
                    Timber.e(e)
                    it.resumeWithException(e)
                }
        }
    }
}