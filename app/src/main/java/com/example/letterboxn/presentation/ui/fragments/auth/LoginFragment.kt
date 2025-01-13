package com.example.letterboxn.presentation.ui.fragments.auth

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.common.utils.isValidEmail
import com.example.letterboxn.common.utils.nancyToastError
import com.example.letterboxn.common.utils.nancyToastInfo
import com.example.letterboxn.common.utils.nancyToastSuccess
import com.example.letterboxn.common.utils.validateInputFieldEmpty
import com.example.letterboxn.common.utils.validateInputFieldMeet
import com.example.letterboxn.databinding.FragmentLoginBinding
import com.example.letterboxn.domain.model.FirestoreUser
import com.example.letterboxn.presentation.ui.activities.MainActivity
import com.example.letterboxn.presentation.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewmodel by viewModels<LoginViewModel>()

    override fun onViewCreatedLight() {
        //googleLogin()
    }

    override fun clickListeners() {
        super.clickListeners()
        binding.buttonBackLogin.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.textForgotPasswordLogin.setOnClickListener {
            nancyToastInfo(requireContext(), getString(R.string.navigating_help_screen))
        }
        binding.textDontHaveAccountLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
        binding.buttonLogin.setOnClickListener {
            val email = binding.textInputEmailLogin.text.toString().trim()
            val password = binding.textInputPasswordLogin.text.toString().trim()

            val isPasswordFilled = validateInputFieldEmpty(binding.textInputLayoutPasswordLogin, password, getString(R.string.please_enter_password))
            val isEmailValid = validateInputFieldMeet(binding.textInputLayoutEmailLogin, isValidEmail(email), getString(R.string.please_enter_valid_email))

            if (!(isEmailValid and isPasswordFilled)) {
                return@setOnClickListener
            }

            lifecycleScope.launch {
                blockLoginButton()
                authenticateUser(email, password)
                resetLoginButton()
            }
        }
    }

    private fun blockLoginButton() {
        binding.progressBarLogin.visibility = View.VISIBLE
        binding.buttonLogin.apply {
            isEnabled = false
            text = null
            setBackgroundColor(requireContext().getColor(R.color.button_disabled))
        }
    }

    private fun resetLoginButton() {
        binding.progressBarLogin.visibility = View.INVISIBLE
        binding.buttonLogin.apply {
            isEnabled = true
            text = getString(R.string.login)
            setBackgroundColor(requireContext().getColor(R.color.letterboxn_login))
        }
    }

    private fun clearInputFields() {
        binding.apply {
            textInputEmailLogin.text = null
            textInputPasswordLogin.text = null
        }
    }

    private suspend fun authenticateUser(email: String, password: String) {
        try {
            val document = viewmodel.firestore.collection("users").document(email).get().await()
            if (document.exists()) {
                val data = document.toObject(FirestoreUser::class.java)
                if (data != null && password == data.password) {
                    val editor = viewmodel.sharedPrefLogon.edit()
                    editor.putString("username", data.username)
                    editor.putString("email", data.email)
                    editor.putBoolean("status_loggedin", true)
                    editor.putString("entrypin", data.entrypin)
                    editor.apply()
                    clearInputFields()
                    nancyToastSuccess(requireContext(), getString(R.string.login_successful))
                    navigateToMainActivity()
                } else {
                    nancyToastError(requireContext(), getString(R.string.invalid_password))
                }
            } else {
                nancyToastError(requireContext(), getString(R.string.user_not_exist))
            }
        } catch (e: Exception) {
            nancyToastError(requireContext(), getString(R.string.auth_failed))
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }


//    private fun authUserInTMDB(apikey: String) {
//        runBlocking {
//            try {
//                val requestToken = api.getRequestToken(apikey).requestToken
//                openWebBrowserWithGetRequest(requestToken)
//                val sessionId = api.postCreateSession(
//                    apiKey = apikey,
//                    requestTokenBody = RequestTokenBody("bf13ee5dd8d5e923f27d07ec2ab1f6cca28eb90f")
//                )
//                val accountId = api.getAccountDetails(
//                    apiKey = apikey,
//                    sessionId = sessionId.sessionId
//                )
//                //TODO -> shared prefi di kecir
//                val sharedPref = requireContext().getSharedPreferences("account_id", Context.MODE_PRIVATE)
//                val editor = sharedPref.edit()
//                editor.putInt("id", accountId.id)
//                editor.putString("api+session", "api_key=$apikey&session_id=${sessionId.sessionId}")
//                editor.apply()
//            } catch (e: Exception) {
//                nancyToastError(requireContext(), getString(R.string.tmdb_auth_failed))
//            }
//        }
//    }
//
//    private fun openWebBrowserWithGetRequest(token: String) {
//        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org/authenticate/$token"))
//        startActivity(browserIntent)
//    }

//    private fun googleLogin() {
//        val gOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
//        val gClient = GoogleSignIn.getClient(requireActivity(), gOptions)
//        val gAccount = GoogleSignIn.getLastSignedInAccount(requireContext())
//        if (gAccount != null) navigateToMainActivity()
//        else {
//            // Register for activity result
//            val activityResultLauncher = registerForActivityResult(
//                ActivityResultContracts.StartActivityForResult()
//            ) { result: ActivityResult ->
//                if (result.resultCode == Activity.RESULT_OK) {
//                    val data: Intent? = result.data
//                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//                    try {
//                        task.getResult(ApiException::class.java)
//                        navigateToMainActivity()
//                    } catch (e: ApiException) {
//        nancyToastError(requireContext(), getString(R.string.exception_error))
//                    }
//                }
//            }
//
//            // Start the sign-in flow
//            val signInIntent = gClient.signInIntent
//            activityResultLauncher.launch(signInIntent)
//    }
}