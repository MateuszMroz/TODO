package com.example.todo.ui.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todo.databinding.FragmentToDoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ToDoFragment : Fragment() {
    private val todoViewModel: ToDoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentToDoBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = todoViewModel
        }.root
    }
}