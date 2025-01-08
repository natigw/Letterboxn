package com.example.letterboxn.data.remote.api

import com.example.letterboxn.data.remote.model.account.favoriteMovies.FavoriteMoviesDto
import com.example.letterboxn.data.remote.model.account.favoriteMovies.favMovie.RequestAddRemoveFavorite
import com.example.letterboxn.data.remote.model.account.favoriteMovies.favMovie.ResponseAddRemoveFavorite
import com.example.letterboxn.data.remote.model.account.ratedMovies.RatedMoviesDto
import com.example.letterboxn.data.remote.model.account.ratedMovies.rateMovie.RequestAddRating
import com.example.letterboxn.data.remote.model.account.ratedMovies.rateMovie.ResponseAddRating
import com.example.letterboxn.data.remote.model.account.ratedMovies.rateMovie.ResponseDeleteRating
import com.example.letterboxn.data.remote.model.account.watchlist.WatchlistDto
import com.example.letterboxn.data.remote.model.account.watchlist.addWatchlist.RequestAddRemoveWatchlist
import com.example.letterboxn.data.remote.model.account.watchlist.addWatchlist.ResponseAddRemoveWatchlist
import com.example.letterboxn.data.remote.model.genre.GenreItemDto
import com.example.letterboxn.data.remote.model.movieCredit.MovieCreditItemDto
import com.example.letterboxn.data.remote.model.movieCredit.personDetails.PersonDetailsDto
import com.example.letterboxn.data.remote.model.movieDetails.MovieDetailsItemDto
import com.example.letterboxn.data.remote.model.person.PersonItemDto
import com.example.letterboxn.data.remote.model.popularList.PopularListItemDto
import com.example.letterboxn.data.remote.model.popularMovie.PopularMovieItemDto
import com.example.letterboxn.data.remote.model.review.MovieReviewDto
import com.example.letterboxn.data.remote.model.review.details.ReviewDetailsDto
import com.example.letterboxn.data.remote.model.search.multi.MultiSearchItemDto
import com.example.letterboxn.data.remote.model.search.simple.SearchItemDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

const val defaultApiKey = "af7d82efc6ee62c7015bbedd34c8e8bc"
const val defaultSessionId = "74c418d99438ea27b60170d0f6a4f675012ec084"
const val defaultAccountId = "21304579"

interface MovieApi {

    @GET("movie/popular")
    suspend fun getMovies(
        @Query("api_key") apiKey: String = defaultApiKey,
        @Query("page") page :Int = 1
    ) : PopularMovieItemDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = defaultApiKey
    ) : MovieDetailsItemDto

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = defaultApiKey
    ) : MovieCreditItemDto

    @GET("person/{person_id}")
    suspend fun getPersonDetails(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String = defaultApiKey
    ) : PersonDetailsDto

    @GET("account/{account_id}/rated/movies")
    suspend fun getRatedMoviesAccount(
        @Path("account_id") accountId : String = defaultAccountId,
        @Query("api_key") apiKey: String = defaultApiKey,
        @Query("session_id") sessionId: String = defaultSessionId,
        @Query("sort_by") sortBy : String = "created_at.desc"
    ) : RatedMoviesDto

    @POST("movie/{movie_id}/rating")
    suspend fun addRateMovie(
        @Path("movie_id") movieId : Int,
        @Query("api_key") apiKey: String = defaultApiKey,
        @Query("session_id") sessionId: String = defaultSessionId,
        @Body requestRateMovie : RequestAddRating
    ) : ResponseAddRating

    @DELETE("movie/{movie_id}/rating")
    suspend fun deleteRateMovie(
        @Path("movie_id") movieId : Int,
        @Query("api_key") apiKey: String = defaultApiKey,
        @Query("session_id") sessionId: String = defaultSessionId
    ) : ResponseDeleteRating

    @GET("account/{account_id}/favorite/movies")
    suspend fun getFavoriteMovies(
        @Path("account_id") accountId : String = defaultAccountId,
        @Query("api_key") apiKey: String = defaultApiKey,
        @Query("session_id") sessionId: String = defaultSessionId,
        @Query("sort_by") sortBy : String = "created_at.desc"
    ) : FavoriteMoviesDto

    @POST("account/{account_id}/favorite")
    suspend fun addOrRemoveFavoriteMovie(
        @Path("account_id") accountId : String = defaultAccountId,
        @Query("api_key") apiKey: String = defaultApiKey,
        @Query("session_id") sessionId: String = defaultSessionId,
        @Body requestFavorite: RequestAddRemoveFavorite
    ) : ResponseAddRemoveFavorite

    @GET("account/{account_id}/watchlist/movies")
    suspend fun getWatchlist(
        @Path("account_id") accountId : String = defaultAccountId,
        @Query("api_key") apiKey: String = defaultApiKey,
        @Query("session_id") sessionId: String = defaultSessionId
    ) : WatchlistDto

    @POST("account/{account_id}/watchlist")
    suspend fun addOrRemoveWatchlist(
        @Path("account_id") accountId : String = defaultAccountId,
        @Query("api_key") apiKey: String = defaultApiKey,
        @Query("session_id") sessionId: String = defaultSessionId,
        @Body requestWatchlist : RequestAddRemoveWatchlist
    ) : ResponseAddRemoveWatchlist

    @GET("person/popular")
    suspend fun getPeople(
        @Query("page") page: Int = 5,
        @Query("api_key") apiKey: String = defaultApiKey
    ) : PersonItemDto

    @GET("tv/popular")
    suspend fun getLists(
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = defaultApiKey
    ) : PopularListItemDto

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = defaultApiKey
    ) : MovieReviewDto

    @GET("review/{review_id}")
    suspend fun getReviewDetails(
        @Path("review_id") reviewId: Int,
        @Query("api_key") apiKey: String = defaultApiKey
    ) : ReviewDetailsDto

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String = defaultApiKey
    ) : GenreItemDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") movie: String,
        @Query("api_key") apiKey: String = defaultApiKey
    ) : SearchItemDto

    @GET("search/multi")
    suspend fun multiSearchMovies(
        @Query("query") movie: String,
        @Query("api_key") apiKey: String = defaultApiKey
    ) : MultiSearchItemDto
}