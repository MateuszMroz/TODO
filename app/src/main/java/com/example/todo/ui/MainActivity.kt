package com.example.todo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.R
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        findViewById<Button>(R.id.button).setOnClickListener {
//            Log.d("4444_TAG", "onClick: ")
//            val user = hashMapOf(
//                "first" to "Ada",
//                "last" to "Lovelace",
//                "born" to 1815
//            )
//
//            db.collection("users")
//                .add(user)
//                .addOnCompleteListener {
//                    Log.d("4444_TAG", "onComplete")
//                }
//                .addOnSuccessListener { documentReference ->
//                    Log.d("4444_TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
//                }
//                .addOnFailureListener { e ->
//                    Log.w("4444_TAG", "Error adding document", e)
//                }
//                .addOnCanceledListener {
//                    Log.d("4444_TAG", "onCanceled")
//                }
//        }
    }
}