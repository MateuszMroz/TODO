package com.example.todo.ui.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.todo.R
import com.example.todo.databinding.FragmentToDoBinding
import com.example.todo.util.EventObserver
import com.example.todo.util.extensions.showFailureSnackbar
import com.example.todo.util.extensions.showSuccessSnackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ToDoFragment : Fragment() {
    private val navArgs: ToDoFragmentArgs by navArgs()
    private lateinit var binding: FragmentToDoBinding

    @Inject
    lateinit var assistedFactory: ToDoViewModel.ToDoAssistedFactory

    private val todoViewModel: ToDoViewModel by viewModels {
        ToDoViewModel.Factory(assistedFactory, navArgs.todoId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentToDoBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = todoViewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    private fun observeViewModel() {
        todoViewModel.successMsg.observe(viewLifecycleOwner, EventObserver {
            binding.root.apply {
                showSuccessSnackbar(
                    message = getString(it),
                )
            }
        })

        todoViewModel.errorMsg.observe(viewLifecycleOwner, EventObserver {
            binding.root.apply {
                showFailureSnackbar(
                    message = it ?: getString(R.string.something_went_wrong),
                )
            }
        })
    }
}