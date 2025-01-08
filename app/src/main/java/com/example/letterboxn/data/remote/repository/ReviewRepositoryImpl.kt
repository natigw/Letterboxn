package com.example.letterboxn.data.remote.repository

import com.example.letterboxn.common.utils.randomDouble
import com.example.letterboxn.common.utils.roundDouble
import com.example.letterboxn.data.local.database.review.ReviewDao
import com.example.letterboxn.data.remote.api.MovieApi
import com.example.letterboxn.domain.model.ReviewLocalItem
import com.example.letterboxn.domain.model.home.recentReviews.ReviewWithMovieItem
import com.example.letterboxn.domain.repository.ReviewRepository
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewDao : ReviewDao,
    private val movieApi: MovieApi
): ReviewRepository {

    override suspend fun getAllReviews(): List<ReviewLocalItem> {
        val reviews = reviewDao.getAllReviews()
        return reviews.map {
            ReviewLocalItem(
                movieId = it.movieId,
                review = it.review,
                rating = it.rating,
                reviewDate = it.reviewDate,
                reviewId = it.reviewId
            )
        }.reversed()
    }

    override suspend fun getReviewsWithMovies(): List<ReviewWithMovieItem> {
        val peopleResponse = movieApi.getPeople(2).resultPeople
        val listsResponse = movieApi.getLists(3).results

        return listsResponse.mapIndexed { index, movie ->
            val author = peopleResponse.getOrNull(index % peopleResponse.size) //cycle through people if needed
            val randomNumber = randomDouble(1.5, 4.0)
            ReviewWithMovieItem(
                authorName = author?.name ?: "Unknown",
                authorImage = author?.profilePath,
                review = author?.knownFor?.get(0)?.overview?.ifEmpty { "Movie was just perfect!" },
                reviewRating = roundDouble(randomNumber,1),
                commentCount = movie.voteCount,
                movieId = movie.id,
                movieTitle = movie.name,
                moviePoster = movie.posterPath,
                movieRating = randomNumber * 2.2,
                movieReleaseDate = movie.firstAirDate
            )
        }
    }
}