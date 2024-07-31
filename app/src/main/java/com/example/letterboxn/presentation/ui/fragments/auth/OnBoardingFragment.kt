package com.example.letterboxn.presentation.ui.fragments.auth

import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.databinding.FragmentOnBoardingBinding
import com.example.letterboxn.presentation.ui.activities.MainActivity
import com.example.letterboxn.common.utils.NancyToast
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class OnBoardingFragment : BaseFragment<FragmentOnBoardingBinding>(bindingToInflate = FragmentOnBoardingBinding::inflate){

    @Inject
    lateinit var firestore : FirebaseFirestore

    @Named("UserLoggedIn")
    @Inject
    lateinit var shpref : SharedPreferences

    override fun onViewCreatedLight() {
        lifecycleScope.launch {
            val status = shpref.getBoolean("status_loggedin", false)
            val username = shpref.getString("username", null)

            if (status && username != null) {
                if (isUserAuthorized(username)) {
                    navigateToMainActivity()
                } else
                    NancyToast.makeText(requireContext(), "User unauthorized! Please log in.", NancyToast.LENGTH_SHORT, NancyToast.ERROR, false).show()
            }
        }

    }

    override fun observeChanges() {
        binding.buttonGetstarted.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
        }
    }

    private suspend fun isUserAuthorized(email: String): Boolean {
        return try {
            val documentSnapshot = firestore.collection("users").document(email).get().await()
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



//chatgpt

//override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//    super.onViewCreated(view, savedInstanceState)
//
//    lifecycleScope.launch {
//        val status = shpref.getBoolean("status", false)
//        val username = shpref.getString("username", null)
//
//        if (status && username != null && isUserAuthorized(username)) {
//            navigateToMainActivity()
//        } else {
//            handleUnauthorizedUser()
//        }
//    }
//}
//
//private suspend fun isUserAuthorized(email: String): Boolean {
//    return try {
//        val documentSnapshot = firestore.collection("users").document(email).get().await()
//        documentSnapshot.exists()
//    } catch (e: FirebaseFirestoreException) {
//        // Handle Firestore-specific exceptions
//        handleFirestoreException(e)
//        false
//    } catch (e: Exception) {
//        // Handle general exceptions
//        handleGeneralException(e)
//        false
//    }
//}
//
//private fun handleFirestoreException(e: FirebaseFirestoreException) {
//    // Log the exception and show a user-friendly message
//    Log.e("FirestoreError", "Error fetching user data", e)
//    NancyToast.makeText(requireContext(), "Error accessing database. Please try again later.", NancyToast.LENGTH_LONG, NancyToast.ERROR, true).show()
//}
//
//private fun handleGeneralException(e: Exception) {
//    // Log the exception and show a user-friendly message
//    Log.e("GeneralError", "Unexpected error occurred", e)
//    NancyToast.makeText(requireContext(), "An unexpected error occurred. Please try again.", NancyToast.LENGTH_LONG, NancyToast.ERROR, true).show()
//}
//
//private fun handleUnauthorizedUser() {
//    // Handle actions for unauthorized users
//    NancyToast.makeText(requireContext(), "User not authorized. Please log in.", NancyToast.LENGTH_LONG, NancyToast.WARNING, true).show()
//}
