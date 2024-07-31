package com.example.letterboxn.data.remote.repository

import com.example.letterboxn.data.remote.api.TmdbApi
import com.example.letterboxn.domain.model.RatedMovieItem
import com.example.letterboxn.domain.repository.RatedMovieRepository
import javax.inject.Inject

class RatedMovieRepositoryImpl @Inject constructor(
    private val api : TmdbApi
) : RatedMovieRepository {

    override suspend fun getRatedMovies(): List<RatedMovieItem> {
        val response = api.getRatedMoviesAccount()
        return response.results.map {
            RatedMovieItem(
                movieId = it.id,
                moviePoster = it.posterPath,
                rating = it.rating
            )
        }
    }
}