package com.example.letterboxn.testplayground

import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.databinding.FragmentBiometricBinding


class BiometricFragment :
    BaseFragment<FragmentBiometricBinding>(FragmentBiometricBinding::inflate) {
    override fun onViewCreatedLight() {

        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                binding.msgtext.text = "The biometric sensor is available and fingerprints saved"
                binding.msgtext.setTextColor(Color.parseColor("#fafafa"))
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                binding.msgtext.text = "This device has no fingerprint sensor"
                binding.buttonLoginFingerprint.visibility = View.GONE
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                binding.msgtext.text = "The biometric sensor is currently unavailable"
                binding.buttonLoginFingerprint.visibility = View.GONE
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {

                binding.msgtext.text =
                    "The biometric sensor is available, but no fingerprints saved"
                binding.buttonLoginFingerprint.visibility = View.GONE
            }
        }

        // creating a variable for our Executor
        val executor = ContextCompat.getMainExecutor(requireContext())

        // this will give us result of AUTHENTICATION
        val biometricPrompt = BiometricPrompt(
            this@BiometricFragment,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                }

                // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(requireContext(), "Login Success", Toast.LENGTH_SHORT).show()
                    binding.buttonLoginFingerprint.text = "Login Successful"
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }
            })

        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG
        val promptInfo = PromptInfo.Builder().setTitle("Letterboxn")
            .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel")
            .build()
        binding.buttonLoginFingerprint.setOnClickListener { biometricPrompt.authenticate(promptInfo) }
    }

    override fun observeChanges() {

    }
}