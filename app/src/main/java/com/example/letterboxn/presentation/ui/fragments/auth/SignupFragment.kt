package com.example.letterboxn.presentation.ui.fragments.auth

import android.view.View
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.utils.nancyToastError
import com.example.common.utils.nancyToastInfo
import com.example.common.utils.nancyToastSuccess
import com.example.common.utils.nancyToastWarning
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.data.remote.api.MovieApi
import com.example.letterboxn.databinding.FragmentSignupBinding
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@AndroidEntryPoint
class SignupFragment : BaseFragment<FragmentSignupBinding>(FragmentSignupBinding::inflate) {

    @Inject
    lateinit var firestore: FirebaseFirestore

    @Inject
    lateinit var api : MovieApi

    override fun onViewCreatedLight() {

        with(binding) {
            buttonSignup.setOnClickListener {
                val username = usernamesu.text.toString()
                val email = emailsu.text.toString()
                val password = passwordsu.text.toString()
                val entrypin = entrypinsu.text.toString()
                //val apikey = userapikeysu.text.toString()

//                var res: Boolean
//
//                runBlocking {
//                    res = api.isValidApiKey(apikey).success
//                }

                if (!checkInputFields(username, email, password, entrypin)) return@setOnClickListener//, apikey)) return@setOnClickListener

                lifecycleScope.launch {

                    blockSignupButton()

                    if (checkIfUserExists(email)) {
                        nancyToastError(requireContext(), getString(R.string.user_already_exist))
                        resetSignupButton()
                        return@launch
                    }

                    try {
                        registerUser(username, email, password, entrypin)//, apikey)
                        //createUserInFirebaseAuth(email, password)
                        clearInputFields()
                        nancyToastSuccess(requireContext(), getString(R.string.register_successful))
                        findNavController().popBackStack()
                    } catch (e: Exception) {
                        nancyToastError(requireContext(), getString(R.string.register_failed))
                    } finally {
                        resetSignupButton()
                    }
                }
            }
        }
    }

    override fun observeChanges() {
        binding.imageBacktologin.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imageGooglelogo.setOnClickListener {
            nancyToastInfo(requireContext(), getString(R.string.google))
        }
        binding.imageApplelogo.setOnClickListener {
            nancyToastInfo(requireContext(), getString(R.string.apple))
        }
        binding.imageFacebooklogo.setOnClickListener {
            nancyToastInfo(requireContext(), getString(R.string.facebook))
        }
    }

    private fun checkInputFields(username: String, email: String, password: String, entrypin: String) : Boolean {//, apikey: String): Boolean {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || entrypin.isEmpty()) {
            nancyToastWarning(requireContext(), getString(R.string.please_fill_all_gaps))
            return false
        }
        if (password.length < 6) {
            nancyToastWarning(requireContext(), getString(R.string.entry_pin_length_should_more_than_5))
            return false
        }
        if (password.isDigitsOnly()) {
            nancyToastWarning(requireContext(), getString(R.string.password_should_contain_letter))
            return false
        }
        if (!entrypin.isDigitsOnly()){
            nancyToastWarning(requireContext(), getString(R.string.entry_pin_should_only_numbers))
            return false
        }
        if (entrypin.length != 4){
            nancyToastWarning(requireContext(), getString(R.string.entry_pin_length_should_be_4))
            return false
        }
//        if (apikey.isEmpty()) {
//        nancyToastWarning(requireContext(), getString(R.string.invalid_api_key))
//            return false
//        }
//        if (apikey.isEmpty() || !validateApiKey(apikey)) nancyToastWarning(requireContext(), getString(R.string.invalid_api_key))
        //return validateApiKey(apikey)   //else true for all the statements above
        return true
    }

    private fun blockSignupButton() {
        binding.apply {
            progressBarsu.visibility = View.VISIBLE
            buttonSignup.isEnabled = false
            buttonSignup.text = null
            buttonSignup.setBackgroundColor(requireContext().getColor(R.color.button_disabled))
        }
    }

    private fun resetSignupButton() {
        binding.apply {
            progressBarsu.visibility = View.INVISIBLE
            buttonSignup.isEnabled = true
            buttonSignup.text = getString(R.string.sign_up)
            buttonSignup.setBackgroundColor(requireContext().getColor(R.color.letterboxn_register))
        }
    }

    private fun clearInputFields() {
        binding.apply {
            usernamesu.text = null
            emailsu.text = null
            passwordsu.text = null
            //userapikeysu.text = null
        }
    }

    private suspend fun checkIfUserExists(email: String): Boolean {
        return try {
            val documentSnapshot = firestore.collection("users").document(email).get().await()
            documentSnapshot.exists()
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun registerUser(username: String, email: String, password: String, entrypin: String) {//, apikey: String) {
        firestore.collection("users").document(email).set(
            mapOf(
                "username" to username,
                "email" to email,
                "password" to password,
                "entrypin" to entrypin,
                //"apikey" to apikey
            )
        ).await()
    }

//    fun validateApiKey(apiKeyToValidate : String) : Boolean{
//        var res = false
//        lifecycleScope.launch {
//            res = api.isValidApiKey(apiKeyToValidate).success
//        }
//        return res
//    }

}