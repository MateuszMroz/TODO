package com.example.todo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<VDB : ViewDataBinding> : Fragment() {
    private var _binding: VDB? = null
    protected val binding
        get() = _binding

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VDB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
        }

        setupViewModel()

        return binding?.root
    }

    abstract fun setupViewModel()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}