package com.example.todo.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.todo.data.models.ToDo
import com.example.todo.util.extensions.COLLECTION_NAME
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestorePagingSource @Inject constructor(private val db: FirebaseFirestore): PagingSource<QuerySnapshot, ToDo>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, ToDo>): QuerySnapshot? {
        return null //?
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, ToDo> {
        return try {
            val currentPage = params.key ?: db.collection(COLLECTION_NAME)
                    .limit(10)
                    .get()
                    .await()

            val lastDocumentSnapshot = currentPage.documents[currentPage.size() - 1]

            val nextPage = db.collection(COLLECTION_NAME).limit(10).startAfter(lastDocumentSnapshot)
                .get()
                .await()

            LoadResult.Page(
                data = currentPage.toObjects(ToDo::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}