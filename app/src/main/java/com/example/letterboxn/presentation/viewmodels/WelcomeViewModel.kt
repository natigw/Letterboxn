package com.example.letterboxn.presentation.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    @Named("UserLoggedIn")
    val sharedPref: SharedPreferences,
    val firestore: FirebaseFirestore
): ViewModel() {

    val status = sharedPref.getBoolean("status_logged_in", false)
    val email = sharedPref.getString("email", null)

}