package com.example.todo.ui.list_todo

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import com.example.todo.R
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("app:imageUrl")
fun setImage(imageView: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        imageView.load(imageUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
            listener(onError = { _, _ ->
                imageView.load(R.drawable.todo_icon)
            })
        }
    } else {
        imageView.load(R.drawable.todo_icon)
    }
}

@BindingAdapter("app:timestamp")
fun setDate(textView: TextView, timestamp: Long) {
    val dateFormat = SimpleDateFormat("yyyy-mm-dd hh:mm", Locale.getDefault())
    val date: String = dateFormat.format(Date(timestamp))

    textView.text = date
}