package com.example.letterboxn.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.letterboxn.data.remote.api.MovieApi
import com.example.letterboxn.domain.model.MovieItem
import javax.inject.Inject

class MoviesPagingSource @Inject constructor(
    private val api: MovieApi
): PagingSource<Int, MovieItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
        return try {
            val position = params.key ?: 1
            val response = api.getMovies(page = position)

            LoadResult.Page(
                data = response.results.map {
                    MovieItem(
                        movieId = it.id,
                        moviePoster = it.posterPath,
                        movieDescription = it.overview,
                        movieTitle = it.title
                    )
                },
                prevKey = if (position == 1) null else (position - 1),
                nextKey = if (position == response.totalPages) null else (position + 1)
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieItem>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}