package com.example.letterboxn.presentation.ui.fragments.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxn.common.utils.nancyToastError
import com.example.letterboxn.common.utils.nancyToastInfo
import com.example.letterboxn.common.utils.nancyToastSuccess
import com.example.letterboxn.common.utils.nancyToastWarning
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.data.remote.api.AuthApi
import com.example.letterboxn.data.remote.model.authentication.RequestTokenBody
import com.example.letterboxn.databinding.FragmentLoginBinding
import com.example.letterboxn.domain.model.FirestoreUser
import com.example.letterboxn.presentation.ui.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    @Inject
    lateinit var firestore: FirebaseFirestore

    @Inject
    lateinit var firebase: FirebaseAuth

    @Inject
    @Named("UserLoggedIn")
    lateinit var sharedPrefLogon : SharedPreferences

    @Inject
    lateinit var api : AuthApi

    override fun onViewCreatedLight() {

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

        loginButton()
    }

    override fun observeChanges() {
        binding.imageBacktoonboard.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.textForgotpassword.setOnClickListener {
            nancyToastInfo(requireContext(), getString(R.string.navigating_help_screen))
        }
        binding.textDonthaveaccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }

    private fun loginButton() {
        binding.buttonLogin.setOnClickListener {

            val email = binding.emailsi.text.toString()
            val password = binding.passwordsi.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                nancyToastWarning(requireContext(), getString(R.string.please_fill_all_gaps))
                return@setOnClickListener
            }

            lifecycleScope.launch {
                blockLoginButton()
                userAuthentication(email, password)
                resetLoginButton()
            }

        }
    }

    private fun blockLoginButton() {
        binding.apply {
            progressBarsi.visibility = View.VISIBLE
            buttonLogin.isEnabled = false
            buttonLogin.text = null
            buttonLogin.setBackgroundColor(requireContext().getColor(R.color.button_disabled))
        }
    }

    private fun resetLoginButton() {
        binding.apply {
            progressBarsi.visibility = View.INVISIBLE
            buttonLogin.isEnabled = true
            buttonLogin.text = getString(R.string.sign_in)
            buttonLogin.setBackgroundColor(requireContext().getColor(R.color.letterboxn_login))
        }
    }

    private fun clearInputFields() {
        binding.apply {
            emailsi.text = null
            passwordsi.text = null
        }
    }

    private suspend fun userAuthentication(email: String, password: String) {
        try {
            val document = firestore.collection("users").document(email).get().await()
            if (document.exists()) {
                val data = document.toObject(FirestoreUser::class.java)
                if (data != null && password == data.password) {
                    val editor = sharedPrefLogon.edit()
                    editor.putString("username", data.username)
                    editor.putString("email", data.email)
                    editor.putBoolean("status_loggedin", true)
                    editor.putString("entrypin", data.entrypin)
                    editor.apply()
                    clearInputFields()
                    //authUserInTMDB(data.apikey)
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


    private fun authUserInTMDB(apikey: String) {
        runBlocking {
            try {
                val requestToken = api.getRequestToken(apikey).requestToken
                openWebBrowserWithGetRequest(requestToken)
                val sessionId = api.postCreateSession(
                    apiKey = apikey,
                    requestTokenBody = RequestTokenBody("bf13ee5dd8d5e923f27d07ec2ab1f6cca28eb90f")
                )
                val accountId = api.getAccountDetails(
                    apiKey = apikey,
                    sessionId = sessionId.sessionId
                )
                //TODO -> shared prefi di kecir
                val sharedPref = requireContext().getSharedPreferences("account_id", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putInt("id", accountId.id)
                editor.putString("api+session", "api_key=$apikey&session_id=${sessionId.sessionId}")
                editor.apply()
            } catch (e: Exception) {
                nancyToastError(requireContext(), getString(R.string.tmdb_auth_failed))
            }
        }
    }

    private fun openWebBrowserWithGetRequest(token: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org/authenticate/$token"))
        startActivity(browserIntent)
    }

}

//            firebase.LoginWithEmailAndPassword(email, password).addOnSuccessListener {
//               findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
//           }.addOnFailureListener {
//               Toast.makeText(requireContext(), "Auth failed!", Toast.LENGTH_SHORT).show()
//           }