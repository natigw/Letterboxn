package com.example.letterboxn.data.remote.repository

import com.example.letterboxn.data.remote.api.MovieApi
import com.example.letterboxn.domain.model.SearchItem
import com.example.letterboxn.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val api : MovieApi
): SearchRepository{
    override suspend fun searchMovies(movie: String): List<SearchItem> {
        val response = api.searchMovies(movie = "inside")
         return response.results.map {
            SearchItem (
                movieId = it.id,
                movieTitle = it.title,
                moviePoster = it.posterPath,
                movieRating = it.voteAverage.toFloat(),
                movieReleaseDate = it.releaseDate,
                mediaType = "movie"
            )
        }
    }
}