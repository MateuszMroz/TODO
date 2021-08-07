package com.example.todo.util.extensions

import android.app.AlertDialog
import androidx.fragment.app.Fragment

fun Fragment.showDialog(
    title: String,
    subtitle: String,
    onPositive: () -> Unit,
    onNegative: (() -> Unit)? = null
) {
    AlertDialog.Builder(requireContext())
        .setTitle(title)
        .setMessage(subtitle)
        .setPositiveButton(android.R.string.ok) { _, _ -> onPositive() }
        .setNegativeButton(android.R.string.cancel) { _, _ -> onNegative?.invoke() }
        .show()
}