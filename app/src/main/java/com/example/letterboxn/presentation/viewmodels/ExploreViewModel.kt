package com.example.letterboxn.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.letterboxn.domain.model.CategoryItem
import com.example.letterboxn.domain.model.MovieItem
import com.example.letterboxn.domain.repository.CategoryRepository
import com.example.letterboxn.domain.repository.ExploreMovieRepository
import com.example.letterboxn.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val exploreRepository : ExploreMovieRepository,
    private val movieRepository : MovieRepository,
    private val categoryRepository : CategoryRepository
) : ViewModel() {

    val moviesList: LiveData<PagingData<MovieItem>> =
        exploreRepository.getMovies()
            .cachedIn(viewModelScope)

//    var count = MutableStateFlow(1)
//
//    fun increaseCount(){
//        count.update { it.plus(1) }
//        getMovies(count.value)
//    }
//    fun decreaseCount(){
//        count.update { it.plus(-1) }
//        getMovies(count.value)
//    }

//    val movies = MutableStateFlow<List<MovieItem>>(emptyList())
    val categories = MutableStateFlow<List<CategoryItem>>(emptyList())

    init {
//        getMovies(1)
        getCategories()
    }

//    private fun getMovies(page : Int) {
//        viewModelScope.launch {
//            val response = movieRepository.getMovies(page = page)
//            movies.emit(response)
//        }
//    }

    private fun getCategories() {
        viewModelScope.launch {
            val response = categoryRepository.getCategories()
            categories.emit(response)
        }
    }
}