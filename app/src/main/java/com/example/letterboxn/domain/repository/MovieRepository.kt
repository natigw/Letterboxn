package com.example.letterboxn.domain.repository

import com.example.letterboxn.domain.model.MovieBackPosterItem
import com.example.letterboxn.domain.model.MovieDetailsForReviewItem
import com.example.letterboxn.domain.model.MovieItem
import com.example.letterboxn.domain.model.RatedMovieItem
import com.example.letterboxn.domain.model.home.popularLists.PopularListItem

interface MovieRepository {

    suspend fun getMovies(page: Int) : List<MovieItem>
    suspend fun getPosters(page: Int) : List<MovieBackPosterItem>

    suspend fun getMoviesDetails(movieID : Int) : MovieDetailsForReviewItem

    suspend fun getFavoriteMovies() : List<MovieItem>

    suspend fun getRatedMovies() : List<RatedMovieItem>

    suspend fun getLists() : List<PopularListItem>
}