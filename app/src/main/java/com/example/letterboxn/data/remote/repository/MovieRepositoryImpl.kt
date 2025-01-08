package com.example.letterboxn.data.remote.repository

import com.example.letterboxn.data.remote.api.MovieApi
import com.example.letterboxn.domain.model.MovieBackPosterItem
import com.example.letterboxn.domain.model.MovieDetailsForReviewItem
import com.example.letterboxn.domain.model.MovieItem
import com.example.letterboxn.domain.model.RatedMovieItem
import com.example.letterboxn.domain.model.home.popularLists.ListItemEach
import com.example.letterboxn.domain.model.home.popularLists.PopularListItem
import com.example.letterboxn.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api : MovieApi
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

    override suspend fun getLists(): List<PopularListItem> {
        val movies = api.getLists().results
        val people = api.getPeople(3).resultPeople

        return movies.chunked(4).mapIndexed { index, chunk ->
            val listAuthor = people.getOrElse(index % people.size) { people.first() } //cycle through authors
            PopularListItem(
                movie1 = ListItemEach(chunk.getOrNull(0)?.name ?: "", chunk.getOrNull(0)?.posterPath),
                movie2 = ListItemEach(chunk.getOrNull(1)?.name ?: "", chunk.getOrNull(1)?.posterPath),
                movie3 = ListItemEach(chunk.getOrNull(2)?.name ?: "", chunk.getOrNull(2)?.posterPath),
                movie4 = ListItemEach(chunk.getOrNull(3)?.name ?: "", chunk.getOrNull(3)?.posterPath),
                listAuthorName = listAuthor.name,
                listAuthorImage = listAuthor.profilePath,
                likeCount = 100,
                commentCount = 21
            )
        }
    }
}