package com.example.todo.ui.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.todo.R
import com.example.todo.databinding.FragmentToDoBinding
import com.example.todo.ui.BaseFragment
import com.example.todo.util.EventObserver
import com.example.todo.util.extensions.hideKeyboard
import com.example.todo.util.extensions.showFailureSnackbar
import com.example.todo.util.extensions.showSuccessSnackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ToDoFragment : BaseFragment<FragmentToDoBinding>() {
    private val navArgs: ToDoFragmentArgs by navArgs()

    @Inject
    lateinit var assistedFactory: ToDoViewModel.ToDoAssistedFactory

    private val todoViewModel: ToDoViewModel by viewModels {
        ToDoViewModel.Factory(assistedFactory, navArgs.todoId)
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentToDoBinding
        get() = FragmentToDoBinding::inflate

    override fun setupViewModel() {
        binding?.apply {
            viewModel = todoViewModel
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    private fun observeViewModel() {
        todoViewModel.successMsg.observe(viewLifecycleOwner, EventObserver {
            binding?.root?.apply {
                showSuccessSnackbar(
                    message = getString(it),
                )
            }
        })

        todoViewModel.errorMsg.observe(viewLifecycleOwner, EventObserver {
            binding?.root?.apply {
                showFailureSnackbar(
                    message = it ?: getString(R.string.error_something_went_wrong),
                )
            }
        })
    }
}