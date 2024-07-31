package com.example.letterboxn.data.remote.repository

import com.example.letterboxn.data.remote.api.TmdbApi
import com.example.letterboxn.domain.model.CategoryItem
import com.example.letterboxn.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val api : TmdbApi
) : CategoryRepository {

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