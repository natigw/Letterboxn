package com.example.letterboxn.presentation.ui.fragments.auth

import android.content.Intent
import android.content.SharedPreferences
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.navigation.fragment.findNavController
import com.example.common.utils.nancyToastError
import com.example.common.utils.nancyToastSuccess
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.databinding.FragmentPinEntryBinding
import com.example.letterboxn.presentation.ui.activities.OnBoardingActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class PinEntryFragment : BaseFragment<FragmentPinEntryBinding>(FragmentPinEntryBinding::inflate) {

    @Inject
    @Named("UserLoggedIn")
    lateinit var sharedPrefEntryPin: SharedPreferences

    private var correctPin: String? = null
    private var enteredPin = ""

    override fun onViewCreatedLight() {
        correctPin = sharedPrefEntryPin.getString("entrypin", "")
        if (correctPin == "") navigateToOnBoardActivity()
        getPinfromUser()
        checkFingerprintBiometric()
    }

    override fun observeChanges() {

    }

    private fun getPinfromUser() {
        val pinViews = listOf(
            binding.pin1,
            binding.pin2,
            binding.pin3,
            binding.pin4
        )

        val numButtons = listOf(
            binding.num0,
            binding.num1,
            binding.num2,
            binding.num3,
            binding.num4,
            binding.num5,
            binding.num6,
            binding.num7,
            binding.num8,
            binding.num9
        )

        numButtons.forEach { button ->
            button.setOnClickListener {
                if (enteredPin.length < 4) {
                    enteredPin += button.text
                    pinViews[enteredPin.length - 1].text = "•"
                }
                if (enteredPin.length == 4) {
                    checkPin()
                }
            }
        }

        binding.numDel.setOnClickListener {
            if (enteredPin.isNotEmpty()) {
                pinViews[enteredPin.length - 1].text = ""
                enteredPin = enteredPin.dropLast(1)
            }
        }
    }

    private fun checkPin() {
        if (enteredPin == correctPin) navigateToHomeFragment()
        else {
            nancyToastError(requireContext(), getString(R.string.incorrect_pin))
            enteredPin = ""
            binding.pin1.text = ""
            binding.pin2.text = ""
            binding.pin3.text = ""
            binding.pin4.text = ""
        }
    }

    private fun checkFingerprintBiometric() {
        val biometricManager = BiometricManager.from(requireContext())
        if (biometricManager.canAuthenticate() != BiometricManager.BIOMETRIC_SUCCESS) {
            binding.imageFingerprintEntrypin.isGone = true
        }

        val executor = ContextCompat.getMainExecutor(requireContext())

        // this will give us result of AUTHENTICATION
        val biometricPrompt = BiometricPrompt(
            this@PinEntryFragment,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    nancyToastSuccess(requireContext(), getString(R.string.auth_successful))
                    navigateToHomeFragment()
                }
            }
        )

        // BIOMETRIC DIALOG
        val promptInfo = PromptInfo.Builder()
            .setTitle(getString(R.string.letterboxn))
            .setDescription(getString(R.string.use_fingerprint_login))
            .setNegativeButtonText(getString(R.string.cancel))
            .build()
        binding.imageFingerprintEntrypin.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    private fun navigateToOnBoardActivity() {
        val intent = Intent(requireContext(), OnBoardingActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun navigateToHomeFragment() {
        findNavController().navigate(R.id.action_pinEntryFragment_to_homeFragment)
    }

}