package com.example.letterboxn.domain.repository

import com.example.letterboxn.domain.model.MovieItem

interface MovieDetailsRepository {
    suspend fun getMoviesDetails() : MovieItem
}