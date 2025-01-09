package com.example.letterboxn.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment <VB : ViewBinding> (private val bindingToInflate : (LayoutInflater)->VB) : BottomSheetDialogFragment() {

    private var _binding : VB? = null
    protected val binding get() = _binding!!

    abstract fun onViewCreatedLight()

    open fun observeChanges() { }
    open fun clickListeners() { }

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
        clickListeners()
        observeChanges()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}