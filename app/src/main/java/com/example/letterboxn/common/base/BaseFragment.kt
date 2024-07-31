package com.example.letterboxn.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment <VB : ViewBinding> (private val bindingToInflate : (LayoutInflater)->VB) : Fragment() {

    private var _binding : VB? = null
    protected val binding get() = _binding!!

    abstract fun onViewCreatedLight()

    abstract fun observeChanges()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = bindingToInflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreatedLight()
        observeChanges()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}