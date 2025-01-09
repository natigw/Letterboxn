package com.example.letterboxn.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.letterboxn.domain.model.CategoryItem
import com.example.letterboxn.domain.model.MovieItem
import com.example.letterboxn.domain.repository.ExploreRepository
import com.example.letterboxn.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val exploreRepository : ExploreRepository
) : ViewModel() {

    val moviesList: LiveData<PagingData<MovieItem>> =
        exploreRepository.getMovies()
            .cachedIn(viewModelScope)
    val categories = MutableStateFlow<List<CategoryItem>>(emptyList())

    init {
        getCategories()
    }

    private fun getCategories() {
        viewModelScope.launch {
            val response = exploreRepository.getCategories()
            categories.emit(response)
        }
    }
}