package com.example.letterboxn.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.letterboxn.domain.model.MovieItem

interface ExploreMovieRepository {
    suspend fun getMovies(page: Int) : List<MovieItem>
    fun getMovies(): LiveData<PagingData<MovieItem>>
}