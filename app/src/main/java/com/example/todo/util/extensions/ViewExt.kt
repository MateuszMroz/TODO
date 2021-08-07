package com.example.todo.util.extensions

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSuccessSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun View.showFailureSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).apply {
        setTextColor(Color.RED)
    }.show()
}

fun View.showActionFailureSnackbar(message: String, actionMsg: String, onActionClick: () -> Unit) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).apply {
        setActionTextColor(Color.RED)
        setTextColor(Color.RED)
        setAction(actionMsg) {
            onActionClick()
        }
    }.show()
}