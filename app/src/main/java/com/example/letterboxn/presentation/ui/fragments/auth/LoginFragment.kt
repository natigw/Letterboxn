package com.example.letterboxn.presentation.ui.fragments.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color.parseColor
import android.net.Uri
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxn.R
import com.example.letterboxn.domain.model.FirestoreUser
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.databinding.FragmentLoginBinding
import com.example.letterboxn.presentation.ui.activities.MainActivity
import com.example.letterboxn.common.utils.NancyToast
import com.example.letterboxn.data.remote.api.TmdbApi
import com.example.letterboxn.data.remote.model.authentication.RequestTokenBody
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
    lateinit var shprefLogon : SharedPreferences

    @Inject
    lateinit var api : TmdbApi

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
//                        NancyToast.makeText(requireContext(), "exception error", NancyToast.LENGTH_SHORT, NancyToast.ERROR, false).show()
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
            NancyToast.makeText(requireContext(), "[navigating to help page]", NancyToast.LENGTH_SHORT, NancyToast.INFO, false).show()
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
                NancyToast.makeText(requireContext(), "Please fill the gaps!", NancyToast.LENGTH_SHORT, NancyToast.WARNING,false).show()
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
            buttonLogin.setBackgroundColor(parseColor("#FFDADADA"))
        }
    }

    private fun resetLoginButton() {
        binding.apply {
            progressBarsi.visibility = View.INVISIBLE
            buttonLogin.isEnabled = true
            buttonLogin.text = "Sign in"
            buttonLogin.setBackgroundColor(parseColor("#F44336"))
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
                    val editor = shprefLogon.edit()
                    editor.putString("username", data.username)
                    editor.putString("email", data.email)
                    editor.putBoolean("status_loggedin", true)
                    editor.putString("entrypin", data.entrypin)
                    editor.apply()
                    clearInputFields()
                    //authUserInTMDB(data.apikey)
                    NancyToast.makeText(requireContext(), "Login successful!", NancyToast.LENGTH_SHORT, NancyToast.SUCCESS, false).show()
                    navigateToMainActivity()
                } else {
                    NancyToast.makeText(requireContext(), "Invalid password!", NancyToast.LENGTH_SHORT, NancyToast.ERROR, false).show()
                }
            } else {
                NancyToast.makeText(requireContext(), "User doesn't exist!", NancyToast.LENGTH_SHORT, NancyToast.ERROR, false).show()
            }
        } catch (e: Exception) {
            NancyToast.makeText(requireContext(), "Auth failed!", NancyToast.LENGTH_SHORT, NancyToast.ERROR, false).show()
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
                val shpref = requireContext().getSharedPreferences("account_id", Context.MODE_PRIVATE)
                val editor = shpref.edit()
                editor.putInt("id", accountId.id)
                editor.putString("api+session", "api_key=$apikey&session_id=${sessionId.sessionId}")
                editor.apply()
            } catch (e: Exception) {
                NancyToast.makeText(requireContext(), "Failed to authenticate with TMDB", NancyToast.LENGTH_SHORT, NancyToast.ERROR, false).show()
            }
        }
    }

    private fun openWebBrowserWithGetRequest(token: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org/authenticate/$token"))
        startActivity(browserIntent)
    }

}


// //            firebase.LoginWithEmailAndPassword(email, password).addOnSuccessListener {
// //               findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
// //           }.addOnFailureListener {
// //               Toast.makeText(requireContext(), "Auth failed!", Toast.LENGTH_SHORT).show()
// //           }