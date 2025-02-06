package com.example.letterboxn.presentation.ui.fragments.auth

import androidx.core.text.isDigitsOnly
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.common.utils.blockButton
import com.example.letterboxn.common.utils.hideKeyboard
import com.example.letterboxn.common.utils.isValidEmail
import com.example.letterboxn.common.utils.nancyToastError
import com.example.letterboxn.common.utils.nancyToastInfo
import com.example.letterboxn.common.utils.nancyToastSuccess
import com.example.letterboxn.common.utils.resetButton
import com.example.letterboxn.common.utils.validateInputFieldEmpty
import com.example.letterboxn.common.utils.validateInputFieldMeet
import com.example.letterboxn.databinding.FragmentRegisterBinding
import com.example.letterboxn.presentation.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private val viewmodel by viewModels<RegisterViewModel>()

    override fun onViewCreatedLight() {
        with(binding) {
            buttonRegister.setOnClickListener {
                val username = textInputUsernameRegister.text.toString().trim()
                val email = textInputEmailRegister.text.toString().trim()
                val password = textInputPasswordRegister.text.toString().trim()
                val entryPin = textInputEntryPinRegister.text.toString().trim()

                if (!checkInputFields(username, email, password, entryPin)) return@setOnClickListener

                viewLifecycleOwner.lifecycleScope.launch {
                    blockButton(
                        context = requireContext(),
                        progressBar = progressBarRegister,
                        button = buttonRegister
                    )
                    binding.textInputLayoutUsernameRegister.clearFocus()
                    binding.textInputLayoutEmailRegister.clearFocus()
                    binding.textInputLayoutPasswordRegister.clearFocus()
                    binding.textInputLayoutEntryPinRegister.clearFocus()
                    hideKeyboard(binding.root)

                    if (viewmodel.checkIfUserExists(email)) {
                        nancyToastError(requireContext(), getString(R.string.user_already_exist))
                        resetButton(
                            context = requireContext(),
                            progressBar = progressBarRegister,
                            button = buttonRegister,
                            buttonColor = R.color.letterboxn_register,
                            buttonText = getString(R.string.register)
                        )
                        return@launch
                    }

                    try {
                        viewmodel.registerUser(username, email, password, entryPin)
                        //createUserInFirebaseAuth(email, password)
                        clearInputFields()
                        nancyToastSuccess(requireContext(), getString(R.string.register_successful))
                        findNavController().popBackStack()
                    } catch (e: Exception) {
                        nancyToastError(requireContext(), getString(R.string.register_failed))
                    } finally {
                        resetButton(
                            context = requireContext(),
                            progressBar = progressBarRegister,
                            button = buttonRegister,
                            buttonColor = R.color.letterboxn_register,
                            buttonText = getString(R.string.register)
                        )
                    }
                }
            }
        }
    }

    override fun observeChanges() {
        binding.buttonBackRegister.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.imageGoogleLogoRegister.setOnClickListener {
            nancyToastInfo(requireContext(), getString(R.string.google))
        }
        binding.imageAppleLogoRegister.setOnClickListener {
            nancyToastInfo(requireContext(), getString(R.string.apple))
        }
        binding.imageFacebookLogoRegister.setOnClickListener {
            nancyToastInfo(requireContext(), getString(R.string.facebook))
        }
    }

    private fun checkInputFields(username: String, email: String, password: String, entryPin: String): Boolean {

        val isUsernameFilled = validateInputFieldEmpty(binding.textInputLayoutUsernameRegister, username, getString(R.string.please_enter_username))
        val isEmailValid = validateInputFieldMeet(binding.textInputLayoutEmailRegister, isValidEmail(email), getString(R.string.please_enter_valid_email))

        val passwordErrors = mutableListOf<String>()
        val pinErrors = mutableListOf<String>()

        if (!password.contains("[a-z]".toRegex())) passwordErrors.add(getString(R.string.password_should_contain_lowercase))
        if (!password.contains("[A-Z]".toRegex())) passwordErrors.add(getString(R.string.password_should_contain_uppercase))
        if (!password.contains("[0-9]".toRegex())) passwordErrors.add(getString(R.string.password_should_contain_digit))
        if (!password.contains("[!\"#$%&'()*+,-./:;\\\\<=>?@\\[\\]^_`{|}~]".toRegex())) passwordErrors.add(getString(R.string.password_should_contain_special_character))
        if (password.length < 8) passwordErrors.add(getString(R.string.min_password_length_is_8))

        if (passwordErrors.isNotEmpty()) {
            binding.textInputLayoutPasswordRegister.error = passwordErrors.joinToString("\n")
            binding.textInputLayoutPasswordRegister.isErrorEnabled = true
            binding.textInputLayoutPasswordRegister.editText?.requestFocus()
        } else {
            binding.textInputLayoutPasswordRegister.error = null
            binding.textInputLayoutPasswordRegister.isErrorEnabled = false
        }

        if (entryPin.length != 4) pinErrors.add(getString(R.string.entry_pin_length_should_be_4))
        if (!entryPin.isDigitsOnly()) pinErrors.add(getString(R.string.entry_pin_should_only_numbers))

        if (pinErrors.isNotEmpty()) {
            binding.textInputLayoutEntryPinRegister.error = pinErrors.joinToString("\n")
            binding.textInputLayoutEntryPinRegister.isErrorEnabled = true
            binding.textInputLayoutEntryPinRegister.editText?.requestFocus()
        } else {
            binding.textInputLayoutEntryPinRegister.error = null
            binding.textInputLayoutEntryPinRegister.isErrorEnabled = false
        }

        if (!isUsernameFilled || !isEmailValid || passwordErrors.isNotEmpty() || pinErrors.isNotEmpty()) return false

        return true
    }

    private fun clearInputFields() {
        binding.apply {
            textInputUsernameRegister.text = null
            textInputEmailRegister.text = null
            textInputPasswordRegister.text = null
        }
    }
}