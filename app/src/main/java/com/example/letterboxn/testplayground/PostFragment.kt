package com.example.letterboxn.testplayground

import androidx.fragment.app.viewModels
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.databinding.FragmentPostBinding
import com.example.letterboxn.presentation.viewmodels.PostViewModel

class PostFragment : BaseFragment<FragmentPostBinding>(FragmentPostBinding::inflate) {

    private val viewModel: PostViewModel by viewModels()

    override fun onViewCreatedLight() {
        // Example usage
        val requestToken = "451bbd2473c373567a83f5da10a4f31ed1228e61"
        val apiKey = "af7d82efc6ee62c7015bbedd34c8e8bc"
        viewModel.createSession(requestToken, apiKey)
    }

    override fun observeChanges() {

    }
}