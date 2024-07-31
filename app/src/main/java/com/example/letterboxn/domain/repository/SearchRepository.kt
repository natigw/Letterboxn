package com.example.letterboxn.domain.repository

import com.example.letterboxn.domain.model.SearchItem
import retrofit2.http.Query

interface SearchRepository {
    suspend fun searchMovies(@Query("query") movie: String) : List<SearchItem>
}