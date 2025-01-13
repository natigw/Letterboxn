package com.example.letterboxn.presentation.ui.fragments.auth

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.databinding.FragmentWelcomeBinding
import com.example.letterboxn.presentation.ui.activities.MainActivity
import com.example.letterboxn.presentation.viewmodels.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>(FragmentWelcomeBinding::inflate){

    private val viewmodel by viewModels<WelcomeViewModel>()

    override fun onViewCreatedLight() {
        lifecycleScope.launch {
            if (viewmodel.status && viewmodel.username != null) {
                if (isUserAuthorized(viewmodel.username!!)) {
                    navigateToMainActivity()
                }
                //else nancyToastError(requireContext(), "User unauthorized! Please log in.")
            }
        }
    }

    override fun clickListeners() {
        super.clickListeners()
        binding.buttonGetStartedWelcome.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
        }
    }

    private suspend fun isUserAuthorized(email: String): Boolean {
        return try {
            val documentSnapshot = viewmodel.firestore.collection("users").document(email).get().await()
            documentSnapshot.exists()
        } catch (e: Exception) {
            false
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}