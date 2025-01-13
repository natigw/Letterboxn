package com.example.letterboxn.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.letterboxn.domain.repository.MovieRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    val firestore: FirebaseFirestore,
    val movieRepository: MovieRepository
) : ViewModel() {

    suspend fun registerUser(username: String, email: String, password: String, entryPin: String) {
        firestore.collection("users").document(email).set(
            mapOf(
                "username" to username,
                "email" to email,
                "password" to password,
                "entrypin" to entryPin
            )
        ).await()
    }

    suspend fun checkIfUserExists(email: String): Boolean {
        return try {
            val documentSnapshot = firestore.collection("users").document(email).get().await()
            documentSnapshot.exists()
        } catch (e: Exception) {
            false
        }
    }
}