package com.example.letterboxn.domain.repository

import com.example.letterboxn.domain.model.RatedMovieItem

interface RatedMovieRepository {
    suspend fun getRatedMovies() : List<RatedMovieItem>
}