package com.example.letterboxn.presentation.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.letterboxn.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LoginViewModel @Inject constructor(
    @Named("UserLoggedIn")
    val sharedPrefLogon: SharedPreferences,
    val firestore: FirebaseFirestore,
    val firebase: FirebaseAuth,
    private val authRepository: AuthRepository
) : ViewModel() {

}