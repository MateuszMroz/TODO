package com.example.todo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener {
            Log.d("4444_TAG", "onClick: ")
            val user = hashMapOf(
                "first" to "Ada",
                "last" to "Lovelace",
                "born" to 1815
            )

            db.collection("users")
                .add(user)
                .addOnCompleteListener {
                    Log.d("4444_TAG", "onComplete")
                }
                .addOnSuccessListener { documentReference ->
                    Log.d("4444_TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("4444_TAG", "Error adding document", e)
                }
                .addOnCanceledListener {
                    Log.d("4444_TAG", "onCanceled")
                }
        }
    }
}