package com.example.todo.ui.list_todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R
import com.example.todo.databinding.FragmentListToDoBinding
import com.example.todo.ui.BaseFragment
import com.example.todo.ui.list_todo.adapter.ToDoAdapter
import com.example.todo.util.EventObserver
import com.example.todo.util.extensions.showActionFailureSnackbar
import com.example.todo.util.extensions.showDialog
import com.example.todo.util.extensions.showFailureSnackbar
import com.example.todo.util.extensions.showSuccessSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListToDoFragment : BaseFragment<FragmentListToDoBinding>() {
    private val listToDoViewModel: ListToDoViewModel by viewModels()

    private lateinit var listToDoAdapter: ToDoAdapter

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentListToDoBinding
        get() = FragmentListToDoBinding::inflate

    override fun setupViewModel() {
        binding?.apply {
            viewModel = listToDoViewModel
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNavigation()
        setupListAdapter()
        initAdapterStateListener()
        fetchToDos()
        observeViewModel()
    }

    private fun observeViewModel() {
        listToDoViewModel.successMsg.observe(viewLifecycleOwner, EventObserver {
            binding?.root?.apply {
                showSuccessSnackbar(
                    message = getString(it),
                )
            }
        })

        listToDoViewModel.errorMsg.observe(viewLifecycleOwner, EventObserver {
            binding?.root?.apply {
                showFailureSnackbar(
                    message = it ?: getString(R.string.something_went_wrong),
                )
            }
        })
    }

    private fun initNavigation() {
        listToDoViewModel.newToDo.observe(viewLifecycleOwner, EventObserver {
            val action = ListToDoFragmentDirections.actionListToDoFragmentToToDoFragment()
            findNavController().navigate(action)
        })

        listToDoViewModel.editToDo.observe(viewLifecycleOwner, EventObserver {
            val action = ListToDoFragmentDirections.actionListToDoFragmentToToDoFragment(it)
            findNavController().navigate(action)
        })

        listToDoViewModel.removeToDo.observe(viewLifecycleOwner, EventObserver { id ->
            showDialog(
                title = getString(R.string.remove_task_title),
                subtitle = getString(R.string.remove_task_question),
                onPositive = {
                    lifecycleScope.launch {
                        listToDoViewModel.onRemoveTask(id)
                    }
                }
            )
        })
    }

    private fun fetchToDos() {
        lifecycleScope.launch {
            listToDoViewModel.todos.collect {
                listToDoAdapter.submitData(it)
            }
        }
    }

    private fun setupListAdapter() {
        listToDoAdapter = ToDoAdapter(listToDoViewModel)
        binding?.listTodoRv?.apply {
            adapter = listToDoAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun initAdapterStateListener() {
        lifecycleScope.launch {
            listToDoAdapter.loadStateFlow.collectLatest { loadStates ->
                binding?.loaderTodosPb?.isVisible = loadStates.refresh is LoadState.Loading
                binding?.loaderNextItemsPb?.isVisible = loadStates.append is LoadState.Loading

                fetchError(loadStates)
            }
        }
    }

    private fun fetchError(loadStates: CombinedLoadStates) {
        val errorState = when {
            loadStates.prepend is LoadState.Error -> loadStates.prepend as LoadState.Error
            loadStates.append is LoadState.Error -> loadStates.append as LoadState.Error
            loadStates.refresh is LoadState.Error -> loadStates.refresh as LoadState.Error
            else -> null
        }
        errorState?.let {
            binding?.root?.apply {
                showActionFailureSnackbar(
                    message = it.error.message ?: getString(R.string.something_went_wrong),
                    actionMsg = context.getString(R.string.retry)
                ) {
                    listToDoViewModel.onRefresh()
                }
            }
        }
    }
}