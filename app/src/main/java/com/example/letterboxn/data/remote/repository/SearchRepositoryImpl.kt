package com.example.letterboxn.data.remote.repository

import android.util.Log
import com.example.letterboxn.data.remote.api.MovieApi
import com.example.letterboxn.domain.model.SearchItem
import com.example.letterboxn.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val api: MovieApi
) : SearchRepository {

    override suspend fun searchMovies(movie: String): List<SearchItem> {
        try {
            val responseSearch = api.searchMovies(movie)
            return responseSearch.results.map {
                SearchItem(
                    movieId = it.id,
                    movieTitle = it.title,
                    moviePoster = it.posterPath,
                    movieRating = it.voteAverage.toFloat(),
                    movieReleaseDate = it.releaseDate,
                    mediaType = "movie"
                )
            }
        } catch (e: Exception) {
            Log.e("network", e.toString())
        }
        return emptyList()
    }

    override suspend fun searchMoviesMulti(movie: String): List<SearchItem> {
        try {
            val responseMultiSearch = api.multiSearchMovies(movie)
            return responseMultiSearch.results.map {
                SearchItem(
                    movieId = it.id,
                    movieTitle = it.title,
                    moviePoster = it.posterPath,
                    movieRating = it.voteAverage.toFloat(),
                    movieReleaseDate = it.releaseDate,
                    mediaType = it.mediaType
                )
            }
        } catch (e: Exception) {
            Log.e("network", e.toString())
        }
        return emptyList()
    }
}