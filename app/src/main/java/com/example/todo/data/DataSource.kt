package com.example.todo.data

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class DataSource @Inject constructor(private val db: FirebaseFirestore) {

}