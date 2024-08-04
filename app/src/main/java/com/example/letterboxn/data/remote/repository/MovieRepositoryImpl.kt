package com.example.letterboxn.data.remote.repository

import com.example.letterboxn.data.remote.api.TmdbApi
import com.example.letterboxn.domain.model.MovieBackPosterItem
import com.example.letterboxn.domain.model.MovieDetailsForReviewItem
import com.example.letterboxn.domain.model.MovieItem
import com.example.letterboxn.domain.model.RatedMovieItem
import com.example.letterboxn.domain.model.ReviewWithMovieItem
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

    override suspend fun getMoviesDetails(movieID: Int): MovieDetailsForReviewItem {
        val response = api.getMovieDetails(movieID)
        return MovieDetailsForReviewItem(
            movieId = response.id,
            movieTitle = response.title,
            moviePoster = response.posterPath,
            movieRating = response.voteAverage.toFloat()/2,
            movieReleaseDate = response.releaseDate
        )
    }

    override suspend fun getFavoriteMovies(): List<MovieItem> {
        val response = api.getFavoriteMovies()
        return response.results.map {
            MovieItem(
                movieId = it.id,
                movieTitle = it.title,
                moviePoster = it.posterPath,
                movieDescription = it.overview
            )
        }
    }

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

//    override suspend fun getReviewsWithMovies(): List<ReviewWithMovieItem> {
//        val response = api.
//        return response.results.map {
//            ListItem(
//                listTitle = it.name,
//                authorName = "Author",
//                authorImage = it.posterPath,
//                likeCount = 100,
//                commentCount = 21,
//                movieItems = listOf(MovieItem(it.posterPath, it.originalName, it.overview, it.id))
//            )
//        }
//    }

}