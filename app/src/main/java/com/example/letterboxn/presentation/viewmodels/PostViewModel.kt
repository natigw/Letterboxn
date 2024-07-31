package com.example.letterboxn.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class PostViewModel : ViewModel() {

    private val client = OkHttpClient()

    fun createSession(requestToken: String, apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val json = """
                {
                    "request_token": "$requestToken"
                }
            """.trimIndent()

            val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())

            val request = Request.Builder()
                .url("https://api.themoviedb.org/3/authentication/session/new?api_key=$apiKey")
                .post(requestBody)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    // Handle failure here
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!it.isSuccessful) throw IOException("Unexpected code $it")

                        val responseBody = response.body?.string()
                        // Handle success here, e.g., update LiveData or notify UI
                        println("Response: $responseBody")
                    }
                }
            })
        }
    }
}
