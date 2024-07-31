package com.example.letterboxn.domain.repository

import com.example.letterboxn.domain.model.MovieBackPosterItem
import com.example.letterboxn.domain.model.MovieItem

interface MovieRepository {
    suspend fun getMovies(page: Int) : List<MovieItem>
    suspend fun getPosters(page: Int) : List<MovieBackPosterItem>
}