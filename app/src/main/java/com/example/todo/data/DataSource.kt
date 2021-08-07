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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.CancellationException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class DataSource @Inject constructor(
    private val db: FirebaseFirestore,
    private val mapper: ToDoMapper,
    @SupervisorJobCoroutinesIO private val coroutineContext: CoroutineContext
) {
    var pagingSource: FirestorePagingSource? = null

    fun fetchToDos(): Flow<PagingData<ToDo>> {
        return Pager(
            PagingConfig(
                pageSize = PAGE_SIZE
            )
        ) {
            FirestorePagingSource(db).also {
                pagingSource = it
            }
        }.flow
    }

    suspend fun realtimeUpdates() = callbackFlow<Boolean> {
        val registration = db.collection(COLLECTION_NAME).addSnapshotListener { _, error ->
            if(error == null) {
                trySend(/*new data*/true)
            } else {
                Timber.e("ERROR_SNAPSHOT_LISTENER")
                cancel(CancellationException())
            }
        }

        awaitClose { registration.remove() }
    }

    suspend fun addToDO(todo: ToDo): Unit = withContext(coroutineContext) {
        suspendCancellableCoroutine {
            db.collection(COLLECTION_NAME)
                .add(mapper.convertToHashMap(todo))
                .addOnSuccessListener { _ ->
                    it.resume(Unit)
                }
                .addOnFailureListener { e ->
                    it.resumeWithException(e)
                }
        }
    }

    suspend fun fetchToDoById(id: String): ToDo = withContext(coroutineContext) {
        suspendCancellableCoroutine {
            val dbCollection = db.collection(COLLECTION_NAME)
            val todoRef = dbCollection
                .whereEqualTo("id", id)

            todoRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result?.documents?.get(0)
                    val todo = result?.toObject(ToDo::class.java)

                    if (todo != null) {
                        it.resume(todo)
                    } else {
                        it.resumeWithException(Throwable())
                    }
                } else {
                    it.resumeWithException(task.exception ?: Throwable())
                }
            }
        }
    }

    suspend fun updateToDo(todo: ToDo): Unit = withContext(coroutineContext) {
        suspendCancellableCoroutine {
            val dbCollection = db.collection(COLLECTION_NAME)
            val todoRef = dbCollection
                .whereEqualTo("id", todo.id)

            todoRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result?.firstOrNull()
                    if (result != null) {
                        dbCollection.document(result.id)
                            .update(mapper.convertToSimpleHashMap(todo))
                        it.resume(Unit)
                    } else {
                        it.resumeWithException(Throwable())
                    }
                } else {
                    it.resumeWithException(task.exception ?: Throwable())
                }
            }
        }
    }

    suspend fun deleteToDo(id: String): Unit = withContext(coroutineContext) {
        suspendCancellableCoroutine {
            val dbCollection = db.collection(COLLECTION_NAME)
            val todoRef = dbCollection
                .whereEqualTo("id", id)

            todoRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.forEach {
                        dbCollection.document(it.id).delete()
                    }
                    it.resume(Unit)
                } else {
                    it.resumeWithException(task.exception ?: Throwable())
                }
            }
        }
    }
}