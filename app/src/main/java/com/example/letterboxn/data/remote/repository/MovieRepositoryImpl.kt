package com.example.letterboxn.data.remote.repository

import com.example.letterboxn.data.remote.api.TmdbApi
import com.example.letterboxn.domain.model.MovieBackPosterItem
import com.example.letterboxn.domain.model.MovieItem
import com.example.letterboxn.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api : TmdbApi
) : MovieRepository {

    override suspend fun getMovies(page: Int): List<MovieItem> {

        val response = api.getMovies(page = page)
        return response.results.map {
            MovieItem(
                moviePoster = it.posterPath,
                movieTitle = it.title,
                movieDescription = it.overview,
                movieId = it.id
            )
        }
    }

    override suspend fun getPosters(page: Int): List<MovieBackPosterItem> {

        val response = api.getMovies(page = page)
        return response.results.map {
            MovieBackPosterItem(
                moviePoster = it.posterPath,
                movieTitle = it.title,
                movieBackPoster = it.backdropPath
            )
        }
    }

}