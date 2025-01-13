package com.example.letterboxn.presentation.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class PinEntryViewModel @Inject constructor(
    @Named("UserLoggedIn")
    val sharedPrefEntryPin: SharedPreferences
) : ViewModel() {

    var correctPin: String? = sharedPrefEntryPin.getString("entrypin", "")
    var enteredPin = ""
}