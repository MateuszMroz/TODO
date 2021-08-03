package com.example.todo

import android.app.Application
import com.google.firebase.FirebaseApp

class ToDoApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}