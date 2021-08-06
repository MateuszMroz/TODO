package com.example.todo.data

import com.example.todo.data.models.ToDo
import com.example.todo.di.coroutine.SupervisorJobCoroutinesIO
import com.example.todo.util.extensions.ToDoMapper
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private val collectionName = "todos"

    @ExperimentalCoroutinesApi
    suspend fun addToDO(todo: ToDo): Result<Unit> = withContext(coroutineContext) {
        suspendCancellableCoroutine {
            db.collection(collectionName)
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