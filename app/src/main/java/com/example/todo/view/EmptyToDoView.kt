package com.example.todo.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.todo.R
import com.example.todo.databinding.ViewEmptyTodoBinding

class EmptyToDoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding = ViewEmptyTodoBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.EmptyToDoView, 0,  0)
        try {
            val title = R.styleable.EmptyToDoView_title
        } finally {
            //recycle()
        }
    }
}