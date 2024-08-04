package com.example.letterboxn.presentation.ui.fragments.auth

import android.graphics.Color.parseColor
import android.view.View
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.databinding.FragmentSignupBinding
import com.example.letterboxn.common.utils.NancyToast
import com.example.letterboxn.data.remote.api.TmdbApi
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
    lateinit var api : TmdbApi

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
                        NancyToast.makeText(requireContext(), "User already exists!", NancyToast.LENGTH_SHORT, NancyToast.ERROR, false).show()
                        resetSignupButton()
                        return@launch
                    }

                    try {
                        registerUser(username, email, password, entrypin)//, apikey)
                        //createUserInFirebaseAuth(email, password)
                        clearInputFields()
                        NancyToast.makeText(requireContext(), "Registration successful!", NancyToast.LENGTH_SHORT, NancyToast.SUCCESS, false).show()
                        findNavController().popBackStack()
                    } catch (e: Exception) {
                        NancyToast.makeText(requireContext(), "Registration failed!", NancyToast.LENGTH_SHORT, NancyToast.ERROR, false).show()
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
            NancyToast.makeText(requireContext(), "Google", NancyToast.LENGTH_SHORT, NancyToast.INFO, false).show()
        }
        binding.imageApplelogo.setOnClickListener {
            NancyToast.makeText(requireContext(), "Apple", NancyToast.LENGTH_SHORT, NancyToast.INFO, false).show()
        }
        binding.imageFacebooklogo.setOnClickListener {
            NancyToast.makeText(requireContext(), "Facebook", NancyToast.LENGTH_SHORT, NancyToast.INFO, false).show()
        }
    }

    private fun checkInputFields(username: String, email: String, password: String, entrypin: String) : Boolean {//, apikey: String): Boolean {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || entrypin.isEmpty()) {
            NancyToast.makeText(requireContext(), "Please fill the gaps!", NancyToast.LENGTH_SHORT, NancyToast.WARNING, false).show()
            return false
        }
        if (password.length < 6) {
            NancyToast.makeText(requireContext(), "Password length should be more than 5!", NancyToast.LENGTH_SHORT, NancyToast.WARNING, false).show()
            return false
        }
        if (password.isDigitsOnly()) {
            NancyToast.makeText(requireContext(), "Password should contain letter(s)!", NancyToast.LENGTH_SHORT, NancyToast.WARNING, false).show()
            return false
        }
        if (!entrypin.isDigitsOnly()){
            NancyToast.makeText(requireContext(), "Entry pin can only be numbers!", NancyToast.LENGTH_SHORT, NancyToast.WARNING, false).show()
            return false
        }
        if (entrypin.length != 4){
            NancyToast.makeText(requireContext(), "The length of entrypin can only be 4!", NancyToast.LENGTH_SHORT, NancyToast.WARNING, false).show()
            return false
        }
//        if (apikey.isEmpty()) {
//            NancyToast.makeText(requireContext(), "Please input a valid API key!", NancyToast.LENGTH_SHORT, NancyToast.WARNING, false).show()
//            return false
//        }
        //if (apikey.isEmpty() || !validateApiKey(apikey)) NancyToast.makeText(requireContext(), "Please input a valid API key!", NancyToast.LENGTH_SHORT, NancyToast.WARNING, false).show()
        //return validateApiKey(apikey)   //else true for all the statements above
        return true
    }

    private fun blockSignupButton() {
        binding.apply {
            progressBarsu.visibility = View.VISIBLE
            buttonSignup.isEnabled = false
            buttonSignup.text = null
            buttonSignup.setBackgroundColor(parseColor("#FFDADADA"))
        }
    }

    private fun resetSignupButton() {
        binding.apply {
            progressBarsu.visibility = View.INVISIBLE
            buttonSignup.isEnabled = true
            buttonSignup.text = "Sign up"
            buttonSignup.setBackgroundColor(parseColor("#407BFF"))
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