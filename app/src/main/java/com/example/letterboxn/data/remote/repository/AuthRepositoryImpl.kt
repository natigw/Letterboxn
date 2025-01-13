package com.example.letterboxn.data.remote.repository

import com.example.letterboxn.data.remote.api.AuthApi
import com.example.letterboxn.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {

}