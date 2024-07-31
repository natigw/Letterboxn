package com.example.letterboxn.testplayground

import android.content.Intent
import android.widget.Toast
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.databinding.FragmentGoogleSigninBinding
import com.example.letterboxn.presentation.ui.activities.MainActivity
import com.example.letterboxn.presentation.ui.activities.OnBoardingActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class GoogleSigninFragment : BaseFragment<FragmentGoogleSigninBinding>(FragmentGoogleSigninBinding::inflate) {
    override fun onViewCreatedLight() {
        // Configure Google Sign-In
        val gOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val gClient = GoogleSignIn.getClient(requireActivity(), gOptions)

        // Check for an existing signed-in account
        val gAccount = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (gAccount != null) {
            val gName = gAccount.displayName
            binding.textView37.text = gName

            binding.button2.setOnClickListener {
                gClient.signOut().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Handle sign out success (e.g., update UI)
                        binding.textView37.text = ""
                        navigateToOnboardActivity()
                    } else {
                        // Handle sign out failure
                        Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun observeChanges() {

    }

    private fun navigateToOnboardActivity() {
        val intent = Intent(requireContext(), OnBoardingActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}