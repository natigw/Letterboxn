package com.example.letterboxn.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AuthenticationModule {

    @Provides
    fun firebase() : FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    fun firestore() : FirebaseFirestore {
        return Firebase.firestore
    }

}