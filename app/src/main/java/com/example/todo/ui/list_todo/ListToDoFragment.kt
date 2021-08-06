package com.example.todo.ui.list_todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todo.databinding.FragmentListToDoBinding
import com.example.todo.util.extensions.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListToDoFragment : Fragment() {
    private val listToDoViewModel: ListToDoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentListToDoBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = listToDoViewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNavigation()
    }

    private fun initNavigation() {
        listToDoViewModel.newToDo.observe(viewLifecycleOwner, EventObserver {
            val action = ListToDoFragmentDirections.actionListToDoFragmentToToDoFragment()
            findNavController().navigate(action)
        })
    }
}