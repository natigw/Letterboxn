package com.example.letterboxn.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.letterboxn.data.remote.paging.MoviesPagingSource
import com.example.letterboxn.data.remote.api.TmdbApi
import com.example.letterboxn.domain.model.CategoryItem
import com.example.letterboxn.domain.model.MovieItem
import com.example.letterboxn.domain.repository.ExploreRepository
import javax.inject.Inject

class ExploreRepositoryImpl @Inject constructor(
    private val api: TmdbApi
) : ExploreRepository {

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


    override suspend fun getCategories(): List<CategoryItem> {
        val response = api.getGenres()
        return response.genres.map {
            CategoryItem(
                categoryId = it.id,
                categoryName = it.name
            )
        }
    }
}