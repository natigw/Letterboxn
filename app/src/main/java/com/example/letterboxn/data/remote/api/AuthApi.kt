package com.example.letterboxn.data.remote.api

import com.example.letterboxn.data.remote.model.account.accountDetails.AccountDto
import com.example.letterboxn.data.remote.model.authentication.IsValidApiKeyResponse
import com.example.letterboxn.data.remote.model.authentication.RequestTokenBody
import com.example.letterboxn.data.remote.model.authentication.RequestTokenResponse
import com.example.letterboxn.data.remote.model.authentication.ResponseSession
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @GET("authentication/token/new")
    suspend fun getRequestToken(
        @Query("api_key") apiKey: String
    ): RequestTokenResponse

    @POST("authentication/session/new")
    suspend fun postCreateSession(
        @Query("api_key") apiKey: String,
        @Body requestTokenBody: RequestTokenBody
    ): ResponseSession

    @GET("account")
    suspend fun getAccountDetails(
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId : String
    ) : AccountDto

    @GET("authentication")
    suspend fun isValidApiKey(
        @Query("api_key") apiKey: String
    ) : IsValidApiKeyResponse

}