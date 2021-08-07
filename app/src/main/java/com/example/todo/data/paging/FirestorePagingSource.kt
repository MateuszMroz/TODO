package com.example.todo.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.todo.data.models.ToDo
import com.example.todo.util.COLLECTION_NAME
import com.example.todo.util.PAGE_SIZE
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

internal const val ORDER_FIELD = "time"

class FirestorePagingSource @Inject constructor(private val db: FirebaseFirestore) :
    PagingSource<QuerySnapshot, ToDo>() {

    override fun getRefreshKey(state: PagingState<QuerySnapshot, ToDo>): QuerySnapshot? {
        return null
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, ToDo> {
        return try {
            val currentPage = params.key ?: db.collection(COLLECTION_NAME)
                .orderBy(ORDER_FIELD, Query.Direction.DESCENDING)
                .limit(PAGE_SIZE.toLong())
                .get()
                .await()

            if (currentPage.size() <= 0) {
                return LoadResult.Page(
                    data = currentPage.toObjects(ToDo::class.java),
                    prevKey = null,
                    nextKey = null
                )
            }

            val lastDocumentSnapshot = currentPage.documents[currentPage.size() - 1]

            val nextPage = db.collection(COLLECTION_NAME).limit(PAGE_SIZE.toLong())
                .orderBy(ORDER_FIELD, Query.Direction.DESCENDING)
                .startAfter(lastDocumentSnapshot)
                .get()
                .await()

            LoadResult.Page(
                data = currentPage.toObjects(ToDo::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            Timber.e(e)
            LoadResult.Error(e)
        }
    }
}