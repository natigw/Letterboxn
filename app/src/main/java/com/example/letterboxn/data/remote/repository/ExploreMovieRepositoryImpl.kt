package com.example.letterboxn.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.letterboxn.presentation.adapters.MoviesPagingSource
import com.example.letterboxn.data.remote.api.TmdbApi
import com.example.letterboxn.domain.model.MovieItem
import com.example.letterboxn.domain.repository.ExploreMovieRepository
import javax.inject.Inject

class ExploreMovieRepositoryImpl @Inject constructor(
    private val api: TmdbApi
) : ExploreMovieRepository {

    override suspend fun getMovies(page: Int): List<MovieItem> {
        val response = api.getMovies()
        return response.results.map {
            MovieItem(
                movieId = it.id,
                movieTitle = it.title,
                moviePoster = it.posterPath,
                movieDescription = it.overview
            )
        }
    }

    override fun getMovies(): LiveData<PagingData<MovieItem>> = Pager(
        config = PagingConfig(pageSize = 10, maxSize = 200),
        pagingSourceFactory = { MoviesPagingSource(api) }
    ).liveData
}